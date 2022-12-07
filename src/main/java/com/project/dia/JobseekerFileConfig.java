package com.project.dia;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JobseekerFileConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/photo/**")
                .addResourceLocations("file:///home/diabatch3/toptalent/photo/");
//                .addResourceLocations("file:///D:/Upload/");
//                .addResourceLocations("file:///home/afiddddd/Desktop/images");
    }
}
