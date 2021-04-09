package com.huaxin.controller;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huaxin.entity.Deduct;
import com.huaxin.util.DeductUtil;
import com.huaxin.util.FinalCodeUtil;
import com.huaxin.util.XmlParseUtil;

@Controller
@RequestMapping(value = FinalCodeUtil.DEDUCT_REQ_MAPING_NAME)
public class DeductHttpServer {
	// 记录日志
	private Log logger = LogFactory.getLog(getClass());

	@RequestMapping(FinalCodeUtil.DEDUCT_REQ_METHOD_NAME)
	private void getHttpSerRes(HttpServletRequest req, HttpServletResponse resp){
		String xxml=req.getParameter("xml");
		resp.setContentType("text/xml");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out=null;
		StringBuffer xml=new StringBuffer();
		try {
			out = resp.getWriter();
			
			/*InputStreamReader reader = new InputStreamReader(req.getInputStream(), "UTF-8");
			char[] buff = new char[1024];
			int length = 0;
			while ((length = reader.read(buff)) != -1) {
				xml.append(new String(buff, 0, length));
			}
			//将接受到的参数按照特定的编码格式，转为化为字符串
			String reqestMess=URLDecoder.decode(xml.toString(), "UTF-8");
			reqestMess=reqestMess.replace("xml=", "");
			Map<String,String> map=XmlParseUtil.Dom4jXmlParse(reqestMess);*/
			
			Map<String,String> map=XmlParseUtil.Dom4jXmlParse(xxml);
			
			String srcChnl=map.get("srcChnl")==null?"":map.get("srcChnl");
			String serialNum=map.get("serialNum")==null?"":map.get("serialNum");
			String bankNo=map.get("bankNo")==null?"":map.get("bankNo");
			String bankName=map.get("bankName")==null?"":map.get("bankName");
			String cusNm=map.get("cusNm")==null?"":map.get("cusNm");
			String mobileNo=map.get("mobileNo")==null?"":map.get("mobileNo");
			String credtNo=map.get("credtNo")==null?"":map.get("credtNo");
			String acntNo=map.get("acntNo")==null?"":map.get("acntNo");
			String acntNm=map.get("acntNm")==null?"":map.get("acntNm");
			String acntPro=map.get("acntPro")==null?"":map.get("acntPro");
			String acntCity=map.get("acntCity")==null?"":map.get("acntCity");
			String loanAmount=map.get("loanAmount")==null?"":map.get("loanAmount");
			String mchntCd=map.get("mchntCd")==null?"":map.get("mchntCd");
			String req_signature=map.get("signature")==null?"":map.get("signature");
			//根据参数，进行加密验证签名是否一致
			Deduct deduct=new Deduct();
			deduct.setSerialNum(serialNum);
			deduct.setBankNo(bankNo);
			deduct.setBankName(bankName);
			deduct.setCusNm(cusNm);
			deduct.setMobileNo(mobileNo);
			deduct.setCredtNo(credtNo);
			deduct.setAcntNo(acntNo);
			deduct.setAcntNm(acntNm);
			deduct.setAcntPro(acntPro);
			deduct.setAcntCity(acntCity);
			deduct.setLoanAmount(loanAmount);
			deduct.setMchntCd(mchntCd);
			deduct.setMchntPass(FinalCodeUtil.DEDUCT_MCHNTPASS);
			//组装签名参数
			List<String> parameterlist=DeductUtil.getParameterList(deduct);
			//生成签名撮
			String res_signature=DeductUtil.hex(parameterlist,deduct.getMchntPass());
			String resCode="OK";
			String resMess="交易完成";
			if(StringUtils.isEmpty(bankNo)){
				//银行编号
				resCode=FinalCodeUtil.DEDUCT_CODE001;
				resMess=FinalCodeUtil.DEDUCT_MESSAGE001;
			}else if(StringUtils.isEmpty(cusNm)){
				//客户名称
				resCode=FinalCodeUtil.DEDUCT_CODE002;
				resMess=FinalCodeUtil.DEDUCT_MESSAGE002;
			}else if(StringUtils.isEmpty(mobileNo)){
				//手机号
				resCode=FinalCodeUtil.DEDUCT_CODE003;
				resMess=FinalCodeUtil.DEDUCT_MESSAGE003;
			}else if(StringUtils.isEmpty(credtNo)){
				//身份证
				resCode=FinalCodeUtil.DEDUCT_CODE004;
				resMess=FinalCodeUtil.DEDUCT_MESSAGE004;
			}else if(StringUtils.isEmpty(acntNo)){
				//账号
				resCode=FinalCodeUtil.DEDUCT_CODE005;
				resMess=FinalCodeUtil.DEDUCT_MESSAGE005;
			}else if(StringUtils.isEmpty(acntNm)){
				//账户名称
				resCode=FinalCodeUtil.DEDUCT_CODE006;
				resMess=FinalCodeUtil.DEDUCT_MESSAGE006;
			}else if(StringUtils.isEmpty(acntPro)){
				//省份
				resCode=FinalCodeUtil.DEDUCT_CODE007;
				resMess=FinalCodeUtil.DEDUCT_MESSAGE007;
			}else if(StringUtils.isEmpty(loanAmount)){
				//金额
				resCode=FinalCodeUtil.DEDUCT_CODE008;
				resMess=FinalCodeUtil.DEDUCT_MESSAGE008;
			}else if(StringUtils.isEmpty(mchntCd)){
				//商户号
				resCode=FinalCodeUtil.DEDUCT_CODE009;
				resMess=FinalCodeUtil.DEDUCT_MESSAGE009;
			}else if(StringUtils.isNotEmpty(cusNm) && StringUtils.isNotEmpty(acntNm) && !cusNm.equals(acntNm)){
				//开户名与客户名称不一致
				resCode=FinalCodeUtil.DEDUCT_CODE011;
				resMess=FinalCodeUtil.DEDUCT_MESSAGE011;
			}else if(!res_signature.equals(req_signature)){
				//签名不一致
				resCode=FinalCodeUtil.DEDUCT_CODE010;
				resMess=FinalCodeUtil.DEDUCT_MESSAGE010;
			}
			//正常如果签名一致，限额正常，则进行调用银行的接口，此处不在调用
			if("OK".equals(resCode)){
				//调用银行接口....
			}
			//返回回盘信息
			String  resxml ="<?xml version=\"1.0\" encoding=\""+FinalCodeUtil.DEDUCT_CHARSET+"\"?>"+ 
					"<res>" +
					   "<resCode>"+resCode+"</resCode>" +
					   "<resMess>"+resMess+"</resMess>";
					   if("OK".equals(resCode)){
						   resxml+="<serialNum>"+serialNum+"</serialNum>"+
						   		   "<cusNm>"+cusNm+"</cusNm>"+
								   "<mobileNo>"+mobileNo+"</mobileNo>"+
								   "<credtNo>"+credtNo+"</credtNo>"+
								   "<Amount>"+loanAmount+"</Amount>";
					   }
					   resxml+= "</res>";
			logger.info("服务端返回报文==>"+URLDecoder.decode(resxml, "UTF-8"));
			out.write(URLDecoder.decode(resxml, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
