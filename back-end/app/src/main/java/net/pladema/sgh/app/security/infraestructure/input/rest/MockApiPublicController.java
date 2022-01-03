package net.pladema.sgh.app.security.infraestructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/public-api/mock")
@Tag(name = "Mock api public", description = "Mock api public")
public class MockApiPublicController {

	@GetMapping(value = "/info")
	public @ResponseBody String getInfo() {
		return "PUBLICAPI";
	}
}