package emeraldore.webflux.chapter2;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class BasicFluxOperatorTest {

    /**
     * Flux
     * 데이터로 시작 : just, empty, from_시리즈
     * 함수로 시작 : defer, create
     */
    @Test
    void testFluxFromDate() {
        Flux.just(1, 2, 3, 4)
                .subscribe(data -> System.out.println("data = " + data));

        List<Integer> basicList = List.of(1, 2, 3, 4);
        Flux.fromIterable(basicList)
                .subscribe(data -> System.out.println("data fromIterable = " + data));
    }

    /**
     * Flux defer -> 안에서 Flux 객체를 반환해야 함 (Mono와 마찬가지)
     * Flux create -> 안에서 동기적인 객체를 반환 (모든 객체 가능 - Mono랑 Flux도 가능하단 소리)
     */
    @Test
    void testFluxFromFunction() {
        Flux.defer(() -> {
            return Flux.just(1, 2, 3, 4);
        }).subscribe(data -> System.out.println("data from defer = " + data));

        Flux.create(sink -> {
            sink.next(1);
            sink.next(2);
            sink.next(3);
            sink.complete(); // 구독자 입장에서 완료됐음을 알기 위해서 complete가 필요함
        }).subscribe(data -> System.out.println("data from sink = " + data));
    }

    @Test
    void testSinkDetail() {
        Flux.<String>create(sink -> {
            AtomicInteger counter = new AtomicInteger(0);
            recursiveFunction(sink);
        })
                .contextWrite(Context.of("counter", new AtomicInteger(0)))
                .subscribe(data -> System.out.println("data from recursive = " + data));
    }

    private void recursiveFunction(FluxSink<String> sink) {
        AtomicInteger counter = sink.contextView().get("counter");
        if (counter.incrementAndGet() < 10) {
            sink.next("sink count " + counter);
            recursiveFunction(sink);
        } else {
            sink.complete();
        }
    }

    /*
    [중간 정리]
    Flux의 흐름 시작 방법
    1. 데이터로 부터 : just, empty, from_시리즈
    2. 함수로 부터 : defer(Flux 객체 return), create(동기적인 객체를 return - next)
     */


    // 중간 연산 (데이터 가공)
    @Test
    void testFluxCollectList() {
        // collectList를 거치면서 Flux가 Mono로 바뀜
        Mono<List<Integer>> listMono = Flux.<Integer>just(1, 2, 3, 4, 5)
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .collectList();

        listMono.subscribe(data -> System.out.println("collectList가 변환한 list data! = " + data));
    }

    /*
    [중간 정리]
    Mono -> Flux 변환 : flatMapMany
    Flux -> Mono 변환 : collectList
     */
}
