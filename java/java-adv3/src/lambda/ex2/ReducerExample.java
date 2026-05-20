package lambda.ex2;

import java.util.List;

public class ReducerExample {

	// 함수를 인자로 받아, 리스트 요소를 하나로 축약(reduce)하는 고차 함수
	public static int reduce(
		List<Integer> list, int initial, MyReducer reducer
	) {
		for (Integer num : list) {
			initial = reducer.reduce(initial, num);
		}
		return initial;
	}

	public static void main(String[] args) {
		List<Integer> numbers = List.of(1, 2, 3, 4);
		System.out.println("리스트: " + numbers);

		// 1. 합 구하기 (초기값 0, 덧셈 로직)
		System.out.println("reduce(numbers, 0, (a, b) -> a + b) = "
			+ reduce(numbers, 0, (a, b) -> a + b));

		// 2. 곱 구하기 (초기값 1, 곱셈 로직)
		System.out.println("reduce(numbers, 1, (a, b) -> a * b) = "
			+ reduce(numbers, 1, (a, b) -> a * b));
	}
}
