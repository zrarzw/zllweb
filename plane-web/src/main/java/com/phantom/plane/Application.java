package com.phantom.plane;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;



@SpringBootApplication
@EnableAsync
@ComponentScan("com.phantom")
public class Application  extends SpringBootServletInitializer{
	  private static Logger logger = Logger.getLogger(Application.class);
	// jar启动
		public static void main(String[] args) throws InterruptedException {
			logger.info("springboot---------->主程序JAR包方式启动");
			SpringApplication.run(Application.class, args);
	    }

		// tomcat war启动
	    @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	    	logger.info("springboot---------->主程序WAR包方式启动");
	        return application.sources(Application.class);
	    }
 
}
