package com.lhs.taxcpcAdmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MainTaxcpcApplication extends SpringBootServletInitializer{

	@Override
	   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	      return application.sources(MainTaxcpcApplication.class);
	   }
	
	public static void main(String[] args) {

		SpringApplication springApplication = new SpringApplication(MainTaxcpcApplication.class);
		/**
		 * Writing application PID to the root location of the application.
		 */
		springApplication.addListeners(new ApplicationPidFileWriter());
		springApplication.run(args);
	}
}
