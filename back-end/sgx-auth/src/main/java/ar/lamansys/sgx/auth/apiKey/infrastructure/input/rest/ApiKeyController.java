package ar.lamansys.sgx.auth.apiKey.infrastructure.input.rest;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.auth.apiKey.application.delete.DeleteUserApiKey;
import ar.lamansys.sgx.auth.apiKey.application.generate.GenerateUserApiKey;
import ar.lamansys.sgx.auth.apiKey.application.list.ListUserApiKeys;
import ar.lamansys.sgx.auth.apiKey.infrastructure.input.rest.dto.GenerateApiKeyDto;
import ar.lamansys.sgx.auth.apiKey.infrastructure.input.rest.dto.GeneratedApiKeyDto;
import ar.lamansys.sgx.auth.apiKey.infrastructure.input.rest.dto.UserApiKeyDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Api-Keys", description = "Api-Keys")
@AllArgsConstructor
@RestController
@RequestMapping("/auth/api-keys")
public class ApiKeyController {
	private final GenerateUserApiKey generateUserApiKey;
	private final ListUserApiKeys listUserApiKeys;
	private final DeleteUserApiKey deleteUserApiKey;

	@PostMapping
	public @ResponseBody GeneratedApiKeyDto generateApiKey(
			@RequestBody GenerateApiKeyDto keyInfo
	) {
		var userKey = generateUserApiKey.execute(keyInfo.name);

		return new GeneratedApiKeyDto(
				userKey.name,
				userKey.apiKey
		);
	}

	@GetMapping
	public @ResponseBody List<UserApiKeyDto> list() {
		return listUserApiKeys.execute().stream().map(
				UserApiKeyDto::fromBo
		).collect(Collectors.toList());
	}

	@DeleteMapping("/{keyName}")
	public void deleteApiKey(@PathVariable(name = "keyName") String keyName) {
		deleteUserApiKey.execute(keyName);
	}
}
