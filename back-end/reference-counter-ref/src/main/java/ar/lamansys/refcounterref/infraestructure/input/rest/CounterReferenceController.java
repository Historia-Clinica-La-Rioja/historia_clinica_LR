package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.application.createcounterreference.CreateCounterReference;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.counterreference.CounterReferenceDto;
import ar.lamansys.refcounterref.infraestructure.input.rest.mapper.CounterReferenceMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/counterreference")
@Tag(name = "Counter Reference", description = "Counter Reference")
public class CounterReferenceController {

    private final CreateCounterReference createCounterReference;
    private final CounterReferenceMapper counterReferenceMapper;

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public boolean createCounterReference(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId,
            @RequestBody @Valid CounterReferenceDto counterReferenceDto
    ) {
        log.debug("Input parameters -> institutionId {}, patientId {}, counterReferenceDto {}", institutionId, patientId, counterReferenceDto);
        var counterReferenceBo = counterReferenceMapper.fromCounterReferenceDto(counterReferenceDto);
        counterReferenceBo.setInstitutionId(institutionId);
        counterReferenceBo.setPatientId(patientId);
        createCounterReference.run(counterReferenceBo);
        return true;
    }

}