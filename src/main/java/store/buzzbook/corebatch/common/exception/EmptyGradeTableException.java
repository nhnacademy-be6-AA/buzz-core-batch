package store.buzzbook.corebatch.common.exception;

public class EmptyGradeTableException extends RuntimeException {
	public EmptyGradeTableException() {
		super("The grade table is empty");
	}
}
