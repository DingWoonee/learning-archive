package emeraldore.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxMonoErrorAndSignalTest {

    @Test
    void testBasicSignal() {
        Flux.just(1, 2, 3, 4)
                // doOnNext는 스트림의 이전에서 방출된 데이터를 포착함 (이 예시에서는 1,2,3,4)
                // 자바 Stream의 peak와 유사하다.
                .doOnNext(publishedData -> System.out.println("publishedData = " + publishedData))
                .doOnComplete(() -> System.out.println("스트림이 끝났습니다."))
                .doOnError(ex -> {
                    System.out.println("ex 에러 상황 발생! = " + ex);
                })
                .subscribe(data -> System.out.println("data = " + data));
    }

    @Test
    void testFluxMonoError() {
        /*
        리액티브 스트림에서 에러가 발생해도 테스트는 성공한다. => 에러가 밖으로 던져지지 않는다.
        하지만, **스트림은 그 즉시 중단된다.**

        [에러가 던져지지 않는 이유]
        모든 Operator에 에러를 잡는 처리가 되어있다. 그리고 밖으로 던지지 않는다.

        [그렇게 하는 이유]
        에러가 던져지면 해당 스레드의 스택 트레이스가 출력된다. (호출 스택 활용)
        하지만, 비동기 스트림은 멀티 스레딩 환경에서 동작될 수 있기 때문에 밖으로 던지지 않고,
        스트림 안에서 처리하도록 한다.

        [그래서 이를 어떻게 잡아서 처리해야 하는가?]
        1. try~catch로 Operator 안에서 직접 잡아서 처리
            -> 중간에 스트림을 멈출 필요까지는 없는 에러인 경우 이런식으로 처리한다.
        2. 전파되는 에러 Signal을 잡아서 처리
            -> onErrorMap, onErrorReturn, onErrorComplete
            onErrorMap: 에러를 다른 에러로 매핑(변환)
            onErrorReturn: 에러가 발생했을 때 방출하고 싶은 데이터를 지정
            onErrorComplete: 에러가 발생하면 에러를 전파하지 않고 바로 Complete 시그널을 전파
        */
        Flux.just(1, 2, 3, 4)
                .map(data -> {
                    if (data == 3) {
                        throw new RuntimeException();
                    }
                    return data * 2;
                })
                .subscribe(data -> System.out.println("data = " + data));
    }

    /*
    Flux.Mono.error()
     */
    @Test
    void testFluxMonoDotError() {
        Flux.just(1, 2, 3, 4)
                .flatMap(data -> {
                    if (data != 3) {
                        return Mono.just(data);
                    } else {
                        // throw new RuntimeException();
                        return Mono.error(new RuntimeException());
                        /*
                        [차이점]
                        throw new RuntimeException(); -> 리액티브 스트림 내부 코드에서 에러 시그널로 바꿔줌.
                        return Mono.error(~); -> 직접 에러 시그널을 발생시킴.
                         */
                    }
                }).subscribe(data -> System.out.println("data = " + data));
    }

    /*
    [정리]

    - 시그널에 대하여
    Mono와 Flux에는 [방출(onNext)/완료(onComplete)/에러(onError)] 시그널이 있고,
    doOnNext/doOnComplete/onOnError로 포착이 가능하다.

    - 에러 처리에 대하여
    리액티브 스트림 안에서 발생하는 예외는 스트림 밖으로 던져지지 않는다.
    때문에 스트림 안에서 적절히 try~catch를 이용해서 처리하거나
    onError~ 류 오퍼레이터를 사용하여 처리해야 한다.
     */
}
