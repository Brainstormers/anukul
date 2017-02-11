package org.brainstormers;

import org.brainstormers.beans.ChaterBean;
import org.brainstormers.beans.FbChatHelper;
import org.brainstormers.servlets.WebHookServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	
	@Autowired
	private ApplicationContext context;

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		final WebHookServlet bean = new WebHookServlet();
		bean.setHelper(context.getBean(FbChatHelper.class));
		return new ServletRegistrationBean(bean, "/webhook", "/webhook/*");
	}

	@Bean
	public ChaterBean chaterBean() {
		final ChaterBean bean = new ChaterBean();
		bean.postConstruct();
		return bean;
	}

	@Bean
	public FbChatHelper fbChatHelper() {
		final FbChatHelper bean = new FbChatHelper();
		bean.setChatter(context.getBean(ChaterBean.class));
		return bean;
	}

}
