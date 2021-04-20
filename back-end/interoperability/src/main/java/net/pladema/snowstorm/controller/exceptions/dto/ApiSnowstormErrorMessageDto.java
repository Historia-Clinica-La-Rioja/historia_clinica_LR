package net.pladema.snowstorm.controller.exceptions.dto;

public class ApiSnowstormErrorMessageDto {
	public final String code;
	public final String text;

	public ApiSnowstormErrorMessageDto(String code, String text) {
		this.code = code;
		this.text = text;
	}
}
