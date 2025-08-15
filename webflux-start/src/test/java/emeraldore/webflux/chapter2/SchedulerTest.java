package emeraldore.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class SchedulerTest {

    /*
    [스레드를 바꿀 수 있는 구간]
    1. subscribe
    2. publish
     */
    @Test
    void testBasicFluxMono() {
        Mono.<Integer>just(2)
                .map(data -> {
                    System.out.println("map Thread Name = " + Thread.currentThread().getName());
                    return data * 2;
                })
                // 여기서부터는 Parallel 스레드에서 실행된다.
                .publishOn(Schedulers.parallel())
                .filter(data -> {
                    System.out.println("filter Thread Name = " + Thread.currentThread().getName());
                    return data % 4 == 0;
                })
                // 구독 이후의 작업은 boundedElastic 스레드에서 실행된다.
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(data -> System.out.println("Mono가 구독한 data! = " + data));
    }
}
