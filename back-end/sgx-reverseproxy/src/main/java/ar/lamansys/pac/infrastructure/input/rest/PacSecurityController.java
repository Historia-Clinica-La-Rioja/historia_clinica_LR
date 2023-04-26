package ar.lamansys.pac.infrastructure.input.rest;


import ar.lamansys.pac.infrastructure.input.rest.dto.TokenDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.pac.application.generatetoken.GenerateToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/token")
@RestController
@Slf4j
@AllArgsConstructor
public class PacSecurityController {

	private final GenerateToken generateToken;

	@GetMapping
	public ResponseEntity<TokenDTO> generateToken(@RequestParam("studyInstanceUID") String studyInstanceUID) {
		log.debug("INPUT -> studyInstanceUID {}", studyInstanceUID);
		String token = generateToken.run(studyInstanceUID);
		log.debug("OUTPUT -> token {}", token);
		return ResponseEntity.ok(new TokenDTO(token));
	}
}
