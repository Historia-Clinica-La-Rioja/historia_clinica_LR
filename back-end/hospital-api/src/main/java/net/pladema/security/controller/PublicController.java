package net.pladema.security.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import net.pladema.flavor.service.FlavorService;
import net.pladema.security.controller.dto.PublicInfoDto;
import net.pladema.sgx.featureflags.AppFeature;

import net.pladema.sgx.recaptcha.controller.dto.RecaptchaPublicConfigDto;
import net.pladema.sgx.recaptcha.service.ICaptchaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@Api(value = "Public", tags = { "Public" })
public class PublicController {

	private final String flavor;

	private final ICaptchaService recaptchaService;

	public PublicController(
			FlavorService flavorService,
			ICaptchaService recaptchaService
	) {
		super();
		this.flavor = flavorService.getFlavor().toString();
		this.recaptchaService = recaptchaService;
	}

	@GetMapping(value = "/info")
	public @ResponseBody
	PublicInfoDto getInfo() {
		return new PublicInfoDto(
			flavor,
			Arrays.stream(AppFeature.values()).filter(AppFeature::isActive).collect(Collectors.toSet())
		);
	}

	@GetMapping(value = "/recaptcha")
	public @ResponseBody
	RecaptchaPublicConfigDto getRecaptchaPublicConfig() {
		RecaptchaPublicConfigDto result = new RecaptchaPublicConfigDto(recaptchaService.getPublicConfig());
		return result;
	}


}