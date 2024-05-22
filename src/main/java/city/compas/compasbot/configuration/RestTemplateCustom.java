package city.compas.compasbot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class RestTemplateCustom {
    private RestTemplate restTemplate;

    @Autowired
    public RestTemplateCustom(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }
}
