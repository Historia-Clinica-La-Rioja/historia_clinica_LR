package ar.lamansys.odontology.infrastructure.controller.exception;

//TODO ver como cambia si lo ponemos como ApiErrorMessageDto
public class ApiOdontologyErrorMessageDto {
	public final String code;
	public final String text;

	public ApiOdontologyErrorMessageDto(String code, String text) {
		this.code = code;
		this.text = text;
	}
}
