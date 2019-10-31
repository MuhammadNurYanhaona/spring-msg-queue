package msgque;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@PropertySources({
	@PropertySource("classpath:server.properties"),
	@PropertySource("classpath:application.properties"),
	@PropertySource("classpath:api.properties")
})
@SpringBootApplication
public class Application {

	// starting point of the spring boot application
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// this bean is needed to be able to parse the internal Spring expresssions found within
	// @Value annotations. Without this bean we could not directly read list or array type
	// properties from a property file
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
