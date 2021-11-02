package net.pladema.sgx.error.controller.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.pladema.sgx.utils.UUIDUtils;

public class ApiErrorMessage {
	public final String traceId;
	public final String code;
	public final String text;
	public final Map<String, Object> args;

	public ApiErrorMessage(String code, String text) {
		this(code, text, Collections.emptyMap());
	}

	public ApiErrorMessage(String code, String text, Map<String, Object> args) {
		this.traceId = UUIDUtils.generateErrorTrace();
		this.code = code;
		this.text = text;
		this.args = args;
	}

	public ApiErrorMessage(String code, String text, Consumer<Map<String, Object>> args) {
		this.traceId = UUIDUtils.generateErrorTrace();
		this.code = code;
		this.text = text;
		Map<String, Object> map = new HashMap<>();
		args.accept(map);
		this.args = map;
	}
}
