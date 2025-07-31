package emeraldore.webflux.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/reactive")
public class ReactiveProgrammingExampleController {

    // 1~9까지 출력하는 api
    @GetMapping("/onenine/list")
    public List<Integer> produceOneToNine() {
        List<Integer> sink = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            sink.add(i + 1);
        }
        return sink;
    }

    // 리액티브 스트림 구현체 Flux, Mono를 사용하여 발생하는 데이터를 바로바로 리액티브하게 처리
    @GetMapping("/onenine/flux")
    public Flux<Integer> produceOneToNineFlux() {
        return Flux.create(sink -> {
            for (int i = 0; i < 9; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                sink.next(i + 1);
            }
            sink.complete();
        });
    }

    // 비동기로 동작 - 논 블로킹하게 동작해야 한다.
}
