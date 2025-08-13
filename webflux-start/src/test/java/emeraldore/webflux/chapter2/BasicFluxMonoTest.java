package emeraldore.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BasicFluxMonoTest {

    @Test
    void testBasicFluxMono() {
        // Flux 스트림을 시작하는 방법은 두 가지가 있다.
        // 1. 빈 함수로부터 시작
        // 2. 데이터로부터 시작

        // just 함수를 통해 데이터로부터 Flux 시작
        // map과 filter 같은 연산자로 데이터를 가공
        // subscribe를 통해 데이터 방출
        Flux.<Integer>just(1, 2, 3, 4, 5)
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .subscribe(data -> System.out.println("Flux가 구독한 data! = " + data));

        // Mono와 Flux의 차이
        // Mono -> 0개부터 1개의 데이터만 방출할 수 있는 객체
        // Flux -> 0개 이상의 데이터(제한 없음)를 방출할 수 있는 객체
        // Mono와 Flux의 차이는 딱 이 차이다.

        // Mono 또한 Flux와 마찬가지로 데이터나 함수로부터 시작할 수 있고, 거의 같은 연산을 사용할 수 있다.
        Mono.<Integer>just(2)
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .subscribe(data -> System.out.println("Mono가 구독한 data! = " + data));
    }

    @Test
    void testFluxMonoBlock() {
        Mono<String> justString = Mono.just("string");
        String string = justString.block();
        System.out.println("string = " + string);
    }
}
