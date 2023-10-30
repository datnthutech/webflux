package hello.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

//@Slf4j
@Service
public class RequestCatcherService {

    private final WebClient webClient;

    Logger log = LoggerFactory.getLogger(RequestCatcherService.class);

    public RequestCatcherService() {
        String baseRequestCatcher = "https://www.google.com/";
        this.webClient = WebClient.create(baseRequestCatcher);
    }

    public Mono<String> gettingFromRequestCatcher() {
        ObjectNode result = JsonNodeFactory.instance.objectNode();
        result.put("SomeData", "Test data for response.");

        // TODO call to requestCatcher by Webflux & get data for response.
        return getFromUrl().map(response -> {
            return String.valueOf(response);
        });

//        return result.toString();
    }

    private Mono<String> getFromUrl() {
        return webClient.mutate()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .build()
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .onErrorContinue(((throwable, o) -> log.error(throwable.getMessage(), throwable)))
                .doOnSuccess(response -> {
                    log.info("Logg respons data: " + response);
                })
                .onErrorResume(throwable -> {
                    log.error(throwable.getMessage(), throwable);
                    return Mono.just(throwable.getMessage());
                });
    }
}
