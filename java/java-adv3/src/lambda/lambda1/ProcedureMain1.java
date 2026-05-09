package lambda.lambda1;

import lambda.Procedure;

public class ProcedureMain1 {

	public static void main(String[] args) {
		Procedure proc = new Procedure() {

			@Override
			public void run() {
				System.out.println("hello! lambda");
			}
		};

		proc.run();
	}
}
