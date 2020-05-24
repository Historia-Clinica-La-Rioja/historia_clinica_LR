package net.pladema.security.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.security.controller.dto.PublicInfoDto;

@RestController
@RequestMapping("/public")
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