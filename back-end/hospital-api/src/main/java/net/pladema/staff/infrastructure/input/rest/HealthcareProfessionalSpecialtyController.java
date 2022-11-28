package net.pladema.staff.infrastructure.input.rest;

import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.getprofessionsbyprofessional.GetProfessionsByProfessional;
import net.pladema.staff.controller.dto.ProfessionalProfessionsDto;
import net.pladema.staff.controller.mapper.HealthcareProfessionalSpecialtyMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/healthcareprofessionalspecialties/institution/{institutionId}/professional/{professionalId}")
@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
@RestController
//@Api(value = "Healthcare professional specialties", tags = {"Healthcare professional specialties"})
public class HealthcareProfessionalSpecialtyController {

    private final GetProfessionsByProfessional getProfessionsByProfessional;

    private final HealthcareProfessionalSpecialtyMapper healthcareProfessionalSpecialtyMapper;

    public HealthcareProfessionalSpecialtyController(GetProfessionsByProfessional getProfessionsByProfessional,
													 HealthcareProfessionalSpecialtyMapper healthcareProfessionalSpecialtyMapper) {
        this.getProfessionsByProfessional = getProfessionsByProfessional;
        this.healthcareProfessionalSpecialtyMapper = healthcareProfessionalSpecialtyMapper;
    }
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<ProfessionalProfessionsDto>> getProfessionsByProfessional(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "professionalId") Integer professionalId) {
        log.debug("Input parameters -> {}", professionalId);
        List<ProfessionalProfessionsDto> result = healthcareProfessionalSpecialtyMapper.fromProfessionalProfessionsBoList(getProfessionsByProfessional.run(professionalId));
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }
}