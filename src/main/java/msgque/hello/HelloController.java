package msgque.hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

	@Value("${accepted.message.source}")
	private String messageSource;

	@RequestMapping("/")
    	public String index() {
        	return "Greetings from Spring Boot! " + messageSource;
	}

}
