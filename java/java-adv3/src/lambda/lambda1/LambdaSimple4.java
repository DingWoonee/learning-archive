package lambda.lambda1;

public class LambdaSimple4 {
	public static void main(String[] args) {
		// 기본
		MyCall call1 = (int value) -> value * 2;
		// 타입 추론
		MyCall call2 = (value) -> value * 2;
		// 매개변수 1개 → () 생략 가능
		MyCall call3 = value -> value * 2;

		System.out.println("result = " + call3.call(30));
	}

	interface MyCall {
		int call(int value);
	}
}
