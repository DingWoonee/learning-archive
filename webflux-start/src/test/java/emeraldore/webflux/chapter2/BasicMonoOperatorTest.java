package emeraldore.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class BasicMonoOperatorTest {

    // Mono를 데이터로 시작하는 것은 just랑 empty가 있다.
    @Test
    void startMonoFromData() {
        // just, empty
        Mono.just(1)
                .subscribe(data -> System.out.println("data = " + data));

        // ex) 사소한 에러가 발생했을 때 로그를 남기고 empty Mono만 전파
        Mono.empty().subscribe(data -> System.out.println("data = " + data));
    }

    // Mono를 함수로 시작하는 것은 fromCallable과 defer가 있다.
    // fromCallable -> 동기적인 객체를 Mono로 반환할 때 사용 (아무 객체나 반환 가능하긴 함)
    // defer -> Mono를 반환하고 싶을 때 사용
    @Test
    void startMonoFromFunction() {
        // 임시 마이그레이션이나
        // restTemplate이나 JPA 같이 블로킹이 발생하는 라이브러리를 Mono로 스레드 분리하여 처리할 때 사용한다.
        Mono<String> monoFromCallable = Mono.fromCallable(() -> {
            // 로직을 수행하고 동기적인 객체를 반환
            return callRestTemplate("안녕!");
        }).subscribeOn(Schedulers.boundedElastic());

        // 아래 두 개의 차이
        // monoFromJust는 해당 스레드에서 항상 도달하지만,
        // monoFromDefer는 해당 Mono가 subscribe될 때 도달할 수 있다.
        // -> 데이터가 언제 만들어지는 지가 다르다.
        Mono<String> monoFromDefer = Mono.defer(() -> {
            return callWebClient("안녕!");
        });
        monoFromDefer.subscribe();

        Mono<String> monoFromJust = Mono.just("안녕");
    }

    @Test
    void testDeferNecessity() {
        String a = "안녕";
        String b = "하세";
        String c = "요";
        Mono<String> stringMono = callWebClient(a + b + c);
        // a+b+c를 만드는 로직도 Mono의 흐름 안에서 관리하고 싶다면?
        // = 실제 subscribe할 때 합치는 로직이 수행되게 하고 싶다면?
        Mono<String> stringMono2 = Mono.defer(() -> {
            String a2 = "안녕";
            String b2 = "하세";
            String c2 = "요";
            return callWebClient(a2 + b2 + c2);
        });
        // 만약 이 연산이 블로킹이 발생하는 연산일 경우, 다른 스레드에서 수행하게 할 수 있다.
        Mono<String> stringMono3 = stringMono2.subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<String> callWebClient(String request) {
        return Mono.just(request + " callWebClient");
    }

    private String callRestTemplate(String request) {
        return request + " callRestTemplate 응답";
    }

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
    void monoToFlux() {
        Mono<Integer> one = Mono.just(1);
        Flux<Integer> integerFlux = one.flatMapMany(data -> {
            return Flux.just(data, data + 1, data + 2);
        });
        integerFlux.subscribe(System.out::println);
    }

    /**
     * [정리]
     * - Mono의 흐름 시작 방법
     *  1. 데이터로부터 시작 -> 일반적인 경우 just / 특이한 상황 empty(Optional.empty()랑 비슷)
     *  2. 함수로부터 시작
     *      -> 동기적인 객체를 Mono로 반환하고 싶을 때 fromCallable
     *      / 코드의 흐름을 Mono안에서 관리하면서 Mono를 반환하고 싶을 때 defer
     * - 데이터 가공
     *  1. map
     *  2. filter
     *  3. flatMapMany (Mono를 Flux로 변환)
     * - 흐름 시작 -> 데이터 가공 -> 구독을 배움
     */
}
