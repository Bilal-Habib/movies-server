package dev.bilal.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
//@Import(SecurityConfig.class)
public class MoviesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesApplication.class, args);
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**")
//						.allowedOrigins("http://localhost:3000") // replace with your ngrok URL
////						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
////						.allowedHeaders("Authorization", "Content-Type")
//						.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Methods");
//			}
//		};
//	}

}
