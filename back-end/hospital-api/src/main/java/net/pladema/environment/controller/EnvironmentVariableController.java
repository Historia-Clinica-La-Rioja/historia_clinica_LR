package net.pladema.environment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/environment/variable")
public class EnvironmentVariableController {

	@Value("${prescription.domain.number}")
	private int domainNumber;

	@GetMapping("/digital-recipe-domain-number")
	public int getDigitalRecipeDomainNumber() {
		return domainNumber;
	}

}
