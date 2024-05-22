package city.compas.compasbot;

import city.compas.compasbot.services.BotServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class CompasbotApplication {

	@Autowired
	private BotServiceFacade facade;

	public static void main(String[] args) {
		SpringApplication.run(CompasbotApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void run() {
		facade.run();
	}
}
