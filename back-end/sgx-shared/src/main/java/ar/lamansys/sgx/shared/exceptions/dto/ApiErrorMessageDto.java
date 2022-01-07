package ar.lamansys.sgx.shared.exceptions.dto;

import java.util.Collections;
import java.util.Map;


public class ApiErrorMessageDto {
	public final String code;
	public final String text;
	public final Map<String, Object> args;

	public ApiErrorMessageDto(String code, String text, Map<String, Object> args) {
		this.code = code;
		this.text = text;
		this.args = args;
	}

	public ApiErrorMessageDto(String code, String text) {
		this(code, text, Collections.emptyMap());
	}
}
