package net.pladema.security.controller;

import io.swagger.annotations.Api;
import net.pladema.security.controller.dto.PublicInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@Api(value = "Public", tags = { "Public" })
public class PublicController {

	private final String flavor;

	public PublicController(
			@Value("${app.flavor}") String flavor
	) {
		super();
		this.flavor = flavor;
	}

	@GetMapping(value = "/info")
	public @ResponseBody
	PublicInfoDto getInfo() {
		return new PublicInfoDto(
			flavor
		);
	}

}