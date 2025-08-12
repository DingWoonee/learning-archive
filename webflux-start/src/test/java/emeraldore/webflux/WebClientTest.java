package emeraldore.webflux;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
public class WebClientTest {

    private final WebClient webClient = WebClient.builder().build();

    @Test
    void testWebClient() {
        Flux<Integer> intFlux = webClient.get()
                .uri("http://localhost:8080/reactive/onenine/flux")
                .retrieve()
                .bodyToFlux(Integer.class);

        intFlux.subscribe(data -> {
            System.out.println("처리 되고 있는 스레드 이름: " + Thread.currentThread().getName());
            System.out.println("WebFlux가 구동 중!! : " + data);
        });

        System.out.println("Netty 이벤트 루프로 스레드 복귀!! / 스레드 이름: " + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
    }
}
