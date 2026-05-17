package lambda.ex2;

import java.util.ArrayList;
import java.util.List;

public class MapExample {
	public static List<String> map(List<String> list, StringFunction func) {
		List<String> result = new ArrayList<>();
		for (String s : list) {
			result.add(func.apply(s));
		}
		return result;
	}

	public static void main(String[] args) {
		List<String> strs = List.of("hello", "java", "lambda");
		System.out.println("원본 리스트: " + strs);
		System.out.println("대분자 변환 결과: " + map(strs, String::toUpperCase));
		System.out.println("특수문자 데코 결과: " + map(strs, s -> "***" + s + "***"));
	}
}
