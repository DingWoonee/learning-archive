package emeraldore.webflux.chapter1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
public class FunctionalProgramingTest {

    @Test
    public void produceOneToNine() {
        List<Integer> sink = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            sink.add(i);
        }

        // *2를 전부 해주고 싶다
        sink = map(sink, (data) -> data * 4);

        // 4의 배수들만 남겨두고 싶다
        sink = filter(sink, (data) -> data % 4 == 0);

        forEach(sink, System.out::println);
    }

    @Test
    public void produceOneToNineStream() {
        IntStream.rangeClosed(1, 9).boxed()
                .map(d -> d * 4)
                .filter(d -> d % 8 == 0)
                .forEach(System.out::println);
    }

    @Test
    public void produceOneToNineFlux() {
        Flux<Object> intFlux = Flux.create(sink -> {
            for (int i = 0; i < 9; i++) {
                sink.next(i + 1);
            }
            sink.complete();
        });
        intFlux.subscribe(data -> System.out.println("WebFlux가 구동 중!! : " + data));
        System.out.println("Netty 이벤트 루프로 스레드 복귀!!");
    }

    @Test
    public void produceOneToNineFluxOperator() {
        Flux.fromIterable(
                IntStream.rangeClosed(1, 9).boxed().toList()
                )
                .map(d -> d * 4) // flux의 operator 대부분이 stream과 유사하게 동작한다.
                .filter(d -> d % 8 == 0)
                .subscribe(System.out::println);
    }

    private void forEach(List<Integer> sink, Consumer<Integer> consumer) {
        for (Integer integer : sink) {
            consumer.accept(integer);
        }
    }

    private List<Integer> filter(List<Integer> sink, Function<Integer, Boolean> predicate) {
        List<Integer> newSink2 = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            if (predicate.apply(sink.get(i))) {
                newSink2.add(sink.get(i));
            }
        }
        sink = newSink2;
        return sink;
    }

    private List<Integer> map(List<Integer> sink, Function<Integer, Integer> function) {
        List<Integer> newSink1 = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            newSink1.add(function.apply(sink.get(i)));
        }
        sink = newSink1;
        return sink;
    }
}
