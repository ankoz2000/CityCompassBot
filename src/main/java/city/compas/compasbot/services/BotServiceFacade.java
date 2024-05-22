package city.compas.compasbot.services;

import city.compas.compasbot.services.bot.BaseHandler;
import city.compas.compasbot.services.bot.StartupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BotServiceFacade {
    private final StartupService startupService;
    private final BaseHandler handler;

    public BotServiceFacade(@Autowired StartupService startupService, BaseHandler handler) {
        this.startupService = startupService;
        this.handler = handler;
    }

    public void run() {
        startupService.startup(handler);
    }
}
