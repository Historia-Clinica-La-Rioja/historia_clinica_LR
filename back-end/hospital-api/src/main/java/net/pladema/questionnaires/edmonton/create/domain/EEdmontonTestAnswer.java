package net.pladema.questionnaires.edmonton.create.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import net.pladema.questionnaires.common.domain.exception.QuestionnaireEnumException;
import net.pladema.questionnaires.common.domain.exception.QuestionnaireException;

@Getter
public enum EEdmontonTestAnswer {

	A1((short) 2,  (short)1,  "Sin errores", 0),
	A2((short) 2,  (short)2,  "Errores mínimos de espaciado", 1),
	A3((short) 2,  (short)3,  "Otros Errores", 2),

	A4((short) 4,  (short)4,  "0", 0),
	A5((short) 4,  (short)5,  "1 o 2", 1),
	A6((short) 4,  (short)6,  "Mayor o igual a 3", 2),

	A7((short) 5,  (short)7,  "Excelente", 0),
	A8((short) 5,  (short)8,  "Razonable", 1),
	A9((short) 5,  (short)9,  "Mala", 2),

	A10((short) 7,  (short)10,  "0 a 10 segundos", 0),
	A11((short) 7,  (short)11,  "11 a 20 segundos", 1),
	A12((short) 7,  (short)12,  "Más de 20 segundos", 2),

	A13((short) 9,  (short)13,  "0 o 1", 0),
	A14((short) 9,  (short)14,  "2 o 3", 1),
	A15((short) 9,  (short)15,  "4 o más", 2),

	A16((short) 11,  (short)16,  "Siempre", 0),
	A17((short)	11,  (short)17,  "A veces", 1),
	A18((short) 11,  (short)18,  "Nunca", 2),

	A19((short) 13,  (short)19,  "No", 0),
	A20((short)	13,  (short)20,  "Sí", 1),

	A21((short) 14,  (short)19,  "No", 0),
	A22((short)	14,  (short)20,  "Sí", 1),

	A23((short) 16,  (short)19,  "No", 0),
	A24((short)	16,  (short)20,  "Sí", 1),

	A25((short) 18,  (short)19,  "No", 0),
	A26((short)	18,  (short)20,  "Sí", 1),

	A27((short) 20,  (short)19,  "No", 0),
	A28((short)	20,  (short)20,  "Sí", 1),

	A29((short) 22,  (short)21, "4 o menor: individuo sano", 0),
	A30((short) 22,  (short)22, "5 o 6: vulnerable", 1),
	A31((short) 22,  (short)23, "7 u 8: fragilidad leve", 2),
	A32((short) 22,  (short)24, "9 o 10: fragilidad moderada", 3),
	A33((short) 22,  (short)25, "11 o mayor: fragilidad severa", 4);

	private final Short questionId;
	private final Short answerId;
	private final String answer;
	private final Integer value;

	EEdmontonTestAnswer(short questionId, short answerId, String answer, Integer value) {
		this.questionId = questionId;
		this.answerId = answerId;
		this.answer = answer;
		this.value = value;
	}

	@JsonCreator
	public static List<EEdmontonTestAnswer> getAll() {
		return Arrays.asList(EEdmontonTestAnswer.values());
	}

	public static EEdmontonTestAnswer getById(Short id) {
		if (id == null) return null;
		return Stream.of(values()).filter(eat -> id.equals(eat.getAnswerId())).findAny().orElseThrow(() -> new QuestionnaireException(QuestionnaireEnumException.QUESTIONNAIRE_TEST_ANSWER_DOESNT_EXIST, String.format("Value %s is not valid as a questionnaire answer", id)));
	}
}
