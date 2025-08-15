package emeraldore.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class OperatorFlatMapTest {

    /*
    [FlatMap이 해주는 것]

    Mono<Mono<T>> -> Mono<T>
    Mono<Flux<T>> -> Flux<T>
    Flux<Mono<T>> -> Flux<T>

    .block() 함수로 안의 객체를 꺼낼 수 있으나 이는 스레드를 블로킹하기 때문에 리액티브 체인 안에서는 사용을 지양해야 한다.

    비동기 객체 안에 비동기 객체가 있는 구조는 그럴 필요가 없는 경우가 대부분이다.
    이 비동기 객체가 겹쳐있는 것을 평탕화 해서 하나의 비동기 객체로 만들어주는 것이 FlatMap이다.

    [언제 사용하나?]
    리액티브 체인에서 Mono나 Flux의 흐름이 겹치는 것은 흔하게 발생한다.
    이때 사용해서 평탄화해서 하나의 흐름으로 합칠 수 있다.
     */
    @Test
    void monoToFlux() {
        Mono<Integer> one = Mono.just(1);
        Flux<Integer> integerFlux = one.flatMapMany(data ->
            Flux.just(data, data + 1, data + 2)
        );
        integerFlux.subscribe(System.out::println);
    }

    @Test
    void testWebClientFlatMap() {
        Flux<String> flatMap = Flux.just(
                        callWebClient("1단계 - 문제 이해하기", 1500),
                        callWebClient("2단계 - 문제 단계별로 풀어가기", 1000),
                        callWebClient("3단계 - 최종 응답", 500))
                .flatMap(monoData -> {
                    return monoData;
                });
        // flatMap의 주의점: flatMap은 입력 순서대로 방출되는 것을 보장하지 않는다.
        // 이 예제에서는 딜레이가 짧은 순서대로 방출한다.
        flatMap.subscribe(data -> System.out.println("flatMap data = " + data));

        // 방출 순서를 보장하는 flatMapSequential
        Flux<String> flatMapSequential = Flux.just(
                        callWebClient("1단계 - 문제 이해하기", 1500),
                        callWebClient("2단계 - 문제 단계별로 풀어가기", 1000),
                        callWebClient("3단계 - 최종 응답", 500))
                .flatMapSequential(monoData -> {
                    return monoData;
                });
        flatMapSequential.subscribe(data -> System.out.println("flatMapSequential data = " + data));

        // 데이터를 따로 가공하지 않고 바로 return하는 경우에는 merge를 쓰는 것이 적절하다.
        // 이 또한 flatMap처럼 순서를 보장하지 않는다.
        Flux<String> merge = Flux.merge(
                        callWebClient("1단계 - 문제 이해하기", 1500),
                        callWebClient("2단계 - 문제 단계별로 풀어가기", 1000),
                        callWebClient("3단계 - 최종 응답", 500));
        // 여기에 추가적인 .map같은 것을 붙이면 flatMap과 비슷한 구조
        merge.subscribe(data -> System.out.println("merge data = " + data));

        // 순서를 보장하는 mergeSequential이 있다.
        Flux<String> mergeSequential = Flux.mergeSequential(
                callWebClient("1단계 - 문제 이해하기", 1500),
                callWebClient("2단계 - 문제 단계별로 풀어가기", 1000),
                callWebClient("3단계 - 최종 응답", 500));
        mergeSequential.subscribe(data -> System.out.println("mergeSequential data = " + data));

        // concat
        // merge는 안의 비동기 객체를 동시에 실행시키지만,
        // concat은 비동기 객체들을 순서대로 실행시키는 좀 비효율적인 동작은 한다.
        Flux<String> concat = Flux.concat(
                callWebClient("1단계 - 문제 이해하기", 1500),
                callWebClient("2단계 - 문제 단계별로 풀어가기", 1000),
                callWebClient("3단계 - 최종 응답", 500));
        concat.subscribe(data -> System.out.println("concat data = " + data));

        // Mono안의 Mono 구조 또한 Mono가 한 개 벗겨진다. (평탄화)
        Mono<String> monoMonoString = Mono.just(Mono.just("안녕"))
                .flatMap(monoData -> monoData);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
    }
    /*
    [정리]
    Flux<Mono<T>>
    Mono<Mono<T>>   -> 이런 구조 안에 있는 Mono는 flatMap, merge로 벗겨낼 수 있다.
                    -> flatMap, merge는 순서를 보장하지 않는다.
                    -> 순서 보장이 필요하면 squential을 사용하자.

    Mono<Flux<T>>   -> flatMapMany로 벗겨낼 수 있다. -> 얘는 Flux<T> 순서가 보장된다.

    Flux<Flux<T>>   -> 이차원 배열의 형태를 유지하면서 평탄화하는 경우
                    -> collectList -> Flux<Mono<List<T>>> -> Flux<List<T>>

    => 비동기가 중첩된 모든 구조는 flatMap, merge, collectList 등으로 평탄화가 가능하다!
    (당연한 소리지만, 이때도 블로킹 요소는 알아서 처리해야 한다.)
     */

    private Mono<String> callWebClient(String request, long delay) {
        return Mono.defer(() -> {
                    try {
                        Thread.sleep(delay);
                        return Mono.just(request + " -> 딜레이: " + delay);
                    } catch (InterruptedException e) {
                        return Mono.empty();
                    }
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
