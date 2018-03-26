package com.example.demo.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import com.example.demo.service.CustomUserService;
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	

	// 这里注册的bean是为了对密码进行加密操作，你注册哪种编码器 就会对用户输入的密码进行哪种加密操作。加密之后再与数据库中的密码比对
	// NoOpPasswordEncoder 就是不对密码进行任何的编码操作
	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
	  return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
	
/*	@Bean
	public static BCryptPasswordEncoder bCryptPasswordEncoder() {
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(8);
		return encoder;
	}*/
	
	
	@Bean
	UserDetailsService customUserService(){ //2
		return new CustomUserService(); 
	}
	
	
	
	// 用户认证
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserService()); //3 添加自定义的user Detail service认证
	}
	
	
	// 请求授权
	@Override
	protected void configure(HttpSecurity http) throws Exception {// 
		http.authorizeRequests().antMatchers("/css/*").permitAll()
						.anyRequest().authenticated() //4
						.and()
						.formLogin()
							.loginPage("/login")
							.failureUrl("/login?error")
							.permitAll() //5
						.and()
						.logout().permitAll(); //6
		

	}
}
