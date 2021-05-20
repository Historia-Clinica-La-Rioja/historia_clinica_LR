package net.pladema.sgh.app.security.infraestructure.input.rest;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/public-api/mock")
@Api(value = "Mock api public", tags = { "Mock api public" })
public class MockApiPublicController {

	@GetMapping(value = "/info")
	public @ResponseBody String getInfo() {
		return "PUBLICAPI";
	}
}