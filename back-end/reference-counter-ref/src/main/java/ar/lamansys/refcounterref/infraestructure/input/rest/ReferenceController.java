package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.application.getreference.GetReference;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.ReferenceDto;
import ar.lamansys.refcounterref.infraestructure.input.rest.mapper.ReferenceMapper;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/institutions/{institutionId}/reference/patient/{patientId}")
@Api(value = "Reference", tags = {"Reference"})
public class ReferenceController {

    private final GetReference getReference;
    private final ReferenceMapper referenceMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public List<ReferenceDto> getReference(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId,
            @RequestParam List<Integer> clinicalSpecialtyIds) {
        log.debug("Input parameters -> institutionId {}, patientId {}, clinicalSpecialtyIds {}", institutionId, patientId, clinicalSpecialtyIds);
        List<ReferenceDto> result = referenceMapper.fromListReferenceBo(getReference.run(institutionId, patientId, clinicalSpecialtyIds));
        log.debug("Output -> result {}", result);
        return result;
    }

}
