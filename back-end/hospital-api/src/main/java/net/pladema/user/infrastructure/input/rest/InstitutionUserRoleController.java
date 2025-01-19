package net.pladema.user.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.person.service.PersonService;
import net.pladema.user.application.getusersbyroles.FetchUsersByRoles;

import net.pladema.user.infrastructure.input.rest.dto.PersonDataDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/institution-users-by-roles/institution/{institutionId}")
public class InstitutionUserRoleController {

	private final FetchUsersByRoles fetchUsersByRoles;

	private final PersonService personService;

	@GetMapping()
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_DE_CAMAS')")
	public ResponseEntity<List<PersonDataDto>> getUsersByRoles(
			@RequestParam(name = "rolesId") List<Short> rolesId,
			@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameter -> institutionId {}, rolesId {}", institutionId, rolesId);
		List<PersonDataDto> result = fetchUsersByRoles.execute(institutionId, rolesId)
				.stream()
				.map(bo -> new PersonDataDto(
						bo.getUserId(),
						personService.getCompletePersonNameById(
								bo.getPersonId()
						))
				)
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(result);
	}
}
