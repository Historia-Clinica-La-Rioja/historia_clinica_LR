package ar.lamansys.sgx.controller.dto;

public class ApiErrorMessageDto {
	public final String code;
	public final String text;

	public ApiErrorMessageDto(String code, String text) {
		this.code = code;
		this.text = text;
	}
}
