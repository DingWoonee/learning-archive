package lambda.lambda1;

import lambda.Procedure;

public class ProcedureMain2 {

	public static void main(String[] args) {
		Procedure proc = () -> System.out.println("hello! lambda");

		proc.run();
	}
}
