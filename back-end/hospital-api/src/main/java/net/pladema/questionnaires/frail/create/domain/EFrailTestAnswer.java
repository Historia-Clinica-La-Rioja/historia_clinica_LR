package net.pladema.questionnaires.frail.create.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import net.pladema.questionnaires.common.domain.exception.QuestionnaireEnumException;
import net.pladema.questionnaires.common.domain.exception.QuestionnaireException;

@Getter
public enum EFrailTestAnswer {

	A1((short) 60, (short)42, "Todo el tiempo", 1),
	A2((short) 60, (short)43, "La mayoría de las veces", 1),
	A3((short) 60, (short)44, "Parte del tiempo", 0),
	A4((short) 60, (short)45, "Un poco de tiempo", 0),
	A5((short) 60, (short)46, "En ningún momento", 0),

	A6((short) 62, (short)20, "Sí", 1),
	A7((short) 62, (short)19, "No", 0),

	A8((short) 64, (short)20, "Sí", 1),
	A9((short) 64, (short)19, "No", 0),

	A10((short) 66, (short)20, "Sí", 1),
	A11((short) 66, (short)19, "No", 0),

	A12((short) 68, (short)47, "Mayor al 5%", 1),
	A13((short) 68, (short)48, "Menor al 5%", 0),

	A14((short) 70, (short)49, "0 a 2: Pre-frágil", 0),
	A15((short) 70, (short)50, "3 a 5: Frágil", 1);

	private final Short questionId;
	private final Short answerId;
	private final String answer;
	private final Integer value;

	EFrailTestAnswer(short questionId, short answerId, String answer, Integer value) {
		this.questionId = questionId;
		this.answerId = answerId;
		this.answer = answer;
		this.value = value;
	}

	@JsonCreator
	public static List<EFrailTestAnswer> getAll() {
		return Arrays.asList(EFrailTestAnswer.values());
	}

	public static EFrailTestAnswer getById(Short id){
		if (id == null)
			return null;
		return Stream.of(values())
				.filter(eat -> id.equals(eat.getAnswerId()))
				.findAny()
				.orElseThrow(() -> new QuestionnaireException(QuestionnaireEnumException.QUESTIONNAIRE_TEST_ANSWER_DOESNT_EXIST, String.format("Value %s is not valid as a questionnaire answer", id)));
	}

	public static List<EFrailTestAnswer> getAnswers(Short questionId){
		if(questionId == null)
			return null;
		return Stream.of(values())
				.filter(eat -> questionId.equals(eat.getQuestionId()))
				.collect(Collectors.toList());
	}
}
