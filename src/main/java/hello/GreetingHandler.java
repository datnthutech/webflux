package hello;

import com.fasterxml.jackson.databind.JsonNode;
import hello.service.RequestCatcherService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GreetingHandler {
    private final RequestCatcherService requestCatcherService;

    public GreetingHandler(RequestCatcherService requestCatcherService) {
        this.requestCatcherService = requestCatcherService;
    }

    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Greeting("Hello, Spring!")));
    }

    public Mono<ServerResponse> getRequestCatcher(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(requestCatcherService.gettingFromRequestCatcher(), String.class);
    }

}
