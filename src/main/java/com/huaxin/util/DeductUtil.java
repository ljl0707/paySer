package com.huaxin.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.huaxin.entity.Deduct;
public class DeductUtil {
	//记录日志
	private static Logger logger = Logger.getLogger(DeductUtil.class);	
	/**
     * 支付接口组装参数
     * @param lender
     * @param readXML
     * @return
     */
	public  static List<String>  getParameterList(Deduct deduct){
		List<String> templist=new ArrayList<String>();
		templist.add("DSF");
		templist.add(deduct.getSerialNum());
		templist.add(deduct.getBankNo());
		templist.add(deduct.getBankName());
		templist.add(deduct.getCusNm());
		templist.add(deduct.getMobileNo());
		templist.add(deduct.getCredtNo());
		templist.add(deduct.getAcntNo());
		templist.add(deduct.getAcntNm());
		templist.add(deduct.getAcntPro());
		templist.add(deduct.getAcntCity());
		templist.add(deduct.getLoanAmount());
		templist.add(deduct.getMchntCd());
		templist.add("0");
		return templist;
	}
	/**
	 * 参数生成密钥方法
	 */
	public static String hex(List<String> values,String key){
		String[] strs = new String[values.size()];
		for(int i=0;i<strs.length;i++){
			strs[i] = values.get(i);
		}
		Arrays.sort(strs);
		StringBuffer source = new StringBuffer();
		for(String str:strs){
			source.append(str).append("|");
		}
		String bigstr = source.substring(0,source.length()-1);
		logger.info("支付接口签名明文===>:"+bigstr);
		String result = DigestUtils.shaHex(DigestUtils.shaHex(bigstr)+"|"+key);
		logger.info("支付接口生成的签名信息===>:"+result);
		return result;
	}
}
