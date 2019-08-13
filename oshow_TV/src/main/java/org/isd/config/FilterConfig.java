package org.isd.config;

import com.isd.oxygenshow.filter.SimpleCORSFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean companyUrlFilterRegister() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		//注入过滤器
		registration.setFilter(new SimpleCORSFilter());
		//拦截规则
		registration.addUrlPatterns("/*");
		//过滤器名称
		registration.setName("simplecorsfilter");
		//过滤顺序
		registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
		return registration;
	}

}
