package net.pladema.questionnaires.familybg.create.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import net.pladema.questionnaires.common.domain.exception.QuestionnaireEnumException;
import net.pladema.questionnaires.common.domain.exception.QuestionnaireException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Getter
public enum EFamilyBgTestAnswer {

	// Convivencia
	A1((short) 24, (short) 19, "No", null),
	A2((short) 24, (short) 26, "En la casa", null),
	A3((short) 24, (short) 27, "Fuera del cuarto", null),

	A4((short) 25, (short) 19, "No", null),
	A5((short) 25, (short) 26, "En la casa", null),
	A6((short) 25, (short) 27, "Fuera del cuarto", null),

	A7((short) 26, (short) 19, "No", null),
	A8((short) 26, (short) 26, "En la casa", null),
	A9((short) 26, (short) 27, "Fuera del cuarto", null),

	A10((short) 27, (short) 19, "No", null),
	A11((short) 27, (short) 26, "En la casa", null),
	A12((short) 27, (short) 27, "Fuera del cuarto", null),

	A13((short) 28, (short) 19, "No", null),
	A14((short) 28, (short) 26, "En la casa", null),
	A15((short) 28, (short) 27, "Fuera del cuarto", null),

	A16((short) 29, (short) 19, "No", null),
	A17((short) 29, (short) 26, "En la casa", null),
	A18((short) 29, (short) 27, "Fuera del cuarto", null),

	A19((short) 54, (short) 19, "No", null),
	A20((short) 54, (short) 26, "En la casa", null),
	A21((short) 54, (short) 27, "Fuera del cuarto", null),

	A22((short) 55, (short) 19, "No", null),
	A23((short) 55, (short) 26, "En la casa", null),
	A24((short) 55, (short) 27, "Fuera del cuarto", null),

	A25((short) 30, (short) 19, "No", null),
	A26((short) 30, (short) 26, "En la casa", null),
	A27((short) 30, (short) 27, "Fuera del cuarto", null),

	// Vivienda
	A28((short) 32, (short) 19, "No", null),
	A29((short) 32, (short) 20, "Sí", null),

	A30((short) 33, (short) 19, "No", null),
	A31((short) 33, (short) 20, "Sí", null),

	A32((short) 34, (short) 19, "No", null),
	A33((short) 34, (short) 20, "Sí", null),

	A34((short) 35, (short) 19, "No", null),
	A35((short) 35, (short) 20, "Sí", null),

	// Estudios de la madre
	A36((short) 37, (short) 28, "Analfabeto", null),
	A37((short) 37, (short) 29, "Primario incompleto", null),
	A38((short) 37, (short) 30, "Primario completo", null),
	A39((short) 37, (short) 31, "Secundario incompleto", null),
	A40((short) 37, (short) 32, "Secundario completo", null),
	A41((short) 37, (short) 33, "Universitario o terciario", null),

	// Estudios del padre
	A42((short) 38, (short) 28, "Analfabeto", null),
	A43((short) 38, (short) 29, "Primario incompleto", null),
	A44((short) 38, (short) 30, "Primario completo", null),
	A45((short) 38, (short) 31, "Secundario incompleto", null),
	A46((short) 38, (short) 32, "Secundario completo", null),
	A47((short) 38, (short) 33, "Universitario o terciario", null),

	// Madre
	A48((short) 40, (short) 19, "No", null),
	A49((short) 40, (short) 20, "Sí", null),

	A50((short) 41, (short) 0, "", null),

	A51((short) 42, (short) 0, "", null),

	A52((short) 43, (short) 34, "Soltera", null),
	A53((short) 43, (short) 35, "Casada", null),
	A54((short) 43, (short) 36, "Unión estable", null),
	A55((short) 43, (short) 37, "Otros", null),

	// Padre
	A56((short) 58, (short) 19, "No", null),
	A57((short) 58, (short) 20, "Sí", null),

	A58((short) 57, (short) 0, "", null),

	A59((short) 45, (short) 0, "", null),

	A60((short) 56, (short) 34, "Soltera", null),
	A61((short) 56, (short) 35, "Casada", null),
	A62((short) 56, (short) 36, "Unión estable", null),
	A63((short) 56, (short) 37, "Otros", null),

	// Hermanos
	A64((short) 47, (short) 0, "", null),

	A65((short) 48, (short) 0, "", null),

	// Vivienda
	A66((short) 50, (short) 0, "", null),

	A67((short) 51, (short) 19, "No", null),
	A68((short) 51, (short) 20, "Sí", null),

	A69((short) 52, (short) 38, "Conectado a la red", null),
	A70((short) 52, (short) 39, "Sin conexión a la red", null),
	A71((short) 52, (short) 40, "Fuera del hogar", null),

	A72((short) 53, (short) 38, "Conectado a la red", null),
	A73((short) 53, (short) 39, "Sin conexión a la red", null),
	A74((short) 53, (short) 40, "Fuera del hogar", null),
	A75((short) 53, (short) 41, "Letrina", null),
	;

	private final Short questionId;
	private final Short answerId;
	private final String answer;
	private final String value;

	EFamilyBgTestAnswer(Short questionId, Short answerId, String answer, String value) {
		this.questionId = questionId;
		this.answerId = answerId;
		this.answer = answer;
		this.value = value;
	}

	@JsonCreator
	public static List<EFamilyBgTestAnswer> getAll(){
		return Arrays.asList(EFamilyBgTestAnswer.values());
	}

	public static EFamilyBgTestAnswer getById(Short id) {
		if (id == null)
			return null;
		return Stream.of(values())
				.filter(eat -> id.equals(eat.getQuestionId()))
				.findAny()
				.orElseThrow(() -> new QuestionnaireException(QuestionnaireEnumException.QUESTIONNAIRE_TEST_ANSWER_DOESNT_EXIST, String.format("Value %s is not valid as a questionnaire answer", id)));
	}
}
