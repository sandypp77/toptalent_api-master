package com.project.dia;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .addFilterBefore(corsFilter(), SessionManagementFilter.class)
                .csrf().disable()
                .cors().disable()
                .authorizeRequests().antMatchers("/api/*").permitAll();
    }

    @Bean
    CorsFilter corsFilter(){
        CorsFilter filter = new CorsFilter();
        return filter;
    }

    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurerAdapter() {
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }




}