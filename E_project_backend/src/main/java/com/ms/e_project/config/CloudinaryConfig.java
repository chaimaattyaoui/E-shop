package com.ms.e_project.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dtnhymuza",
                "api_key", "335374375933588",
                "api_secret", "onVGXHQKwqrVEhfqLRcKGKF0AVI"
        ));
    }
}

