package lambda.ex2;

import java.util.ArrayList;
import java.util.List;

public class FilterExample {

	public static List<Integer> filter(List<Integer> list, MyPredicate predicate) {
		List<Integer> result = new ArrayList<>();
		for (Integer i : list) {
			if (predicate.test(i)) {
				result.add(i);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		List<Integer> list = List.of(-3, -2, -1, 1, 2, 3, 5);
		System.out.println("원본 리스트: " + list);

		System.out.println("음수만: " + filter(list, num -> num < 0));
		System.out.println("짝수만: " + filter(list, num -> num % 2 == 0));
	}
}
