package net.pladema.error.controller.dto;

public class ApiErrorMessage {
	public final String code;
	public final String text;

	public ApiErrorMessage(String code, String text) {
		this.code = code;
		this.text = text;
	}
}
