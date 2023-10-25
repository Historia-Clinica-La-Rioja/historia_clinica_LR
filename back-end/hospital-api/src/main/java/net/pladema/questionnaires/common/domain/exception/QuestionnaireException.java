package net.pladema.questionnaires.common.domain.exception;

public class QuestionnaireException extends RuntimeException {

	public QuestionnaireException(QuestionnaireEnumException code, String message) {
		super(message);
	}
}
