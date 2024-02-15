package net.pladema.sgh.app.security.infraestructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.sgh.app.security.infraestructure.input.rest.dto.PublicApiInfoDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/public-api/info")
@Tag(name = "PublicApi Info", description = "HSI Info")
public class PublicApiInfoController {
	private final PublicApiInfoDto publicApiInfoDto;

	public PublicApiInfoController(
			@Value("${app.env.domain}") String domain
	) {

		this.publicApiInfoDto = new PublicApiInfoDto(
				domain
		);
	}


	@GetMapping(value = "/urls")
	public @ResponseBody PublicApiInfoDto getInfo() {
		return publicApiInfoDto;
	}
}