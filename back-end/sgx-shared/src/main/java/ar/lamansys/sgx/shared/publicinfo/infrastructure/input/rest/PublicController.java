package ar.lamansys.sgx.shared.publicinfo.infrastructure.input.rest;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.gitinfo.configuration.GitInfoProperties;
import ar.lamansys.sgx.shared.publicinfo.application.ApplicationVersionService;
import ar.lamansys.sgx.shared.publicinfo.infrastructure.input.dto.ApplicationVersionDto;
import ar.lamansys.sgx.shared.publicinfo.infrastructure.input.dto.PublicInfoDto;
import ar.lamansys.sgx.shared.recaptcha.controller.dto.RecaptchaPublicConfigDto;
import ar.lamansys.sgx.shared.recaptcha.service.ICaptchaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Tag(name = "Public", description = "Public")
@RestController
@RequestMapping("/public")
public class PublicController {

	private final ICaptchaService recaptchaService;
	private final ApplicationVersionService applicationVersionService;
	private final GitInfoProperties gitInfoService;

	@GetMapping(value = "/info")
	public @ResponseBody
	PublicInfoDto getInfo() {
		return new PublicInfoDto(
			Arrays.stream(AppFeature.values()).filter(AppFeature::isActive).collect(Collectors.toSet())
		);
	}

	@GetMapping(value = "/recaptcha")
	public @ResponseBody
	RecaptchaPublicConfigDto getRecaptchaPublicConfig() {
		return new RecaptchaPublicConfigDto(
				recaptchaService.getPublicConfig()
		);
	}

	@GetMapping(value = "/version")
	public @ResponseBody
	ApplicationVersionDto getApplicationVersion() {
		return new ApplicationVersionDto(
				applicationVersionService.getCurrentVersion(),
				gitInfoService.getBranch(),
				gitInfoService.getCommit().getId()
		);
	}

}