package com.huaxin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
@SpringBootApplication
//@EnableScheduling//开启定时任务
public class PaySerApp {

	public static void main(String[] args) {
		SpringApplication.run(PaySerApp.class, args);
	}
}
