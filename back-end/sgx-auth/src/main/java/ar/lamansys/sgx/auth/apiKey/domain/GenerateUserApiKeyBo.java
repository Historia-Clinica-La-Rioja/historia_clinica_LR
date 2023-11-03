package ar.lamansys.sgx.auth.apiKey.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GenerateUserApiKeyBo {
	public final String name;
	public final String apiKey;
}
