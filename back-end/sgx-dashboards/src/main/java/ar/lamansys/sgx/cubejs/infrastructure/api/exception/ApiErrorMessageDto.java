package ar.lamansys.sgx.cubejs.infrastructure.api.exception;

public class ApiErrorMessageDto {
	public final String code;
	public final String text;

	public ApiErrorMessageDto(String code, String text) {
		this.code = code;
		this.text = text;
	}
}
