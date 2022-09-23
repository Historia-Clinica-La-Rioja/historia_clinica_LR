package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.application.getreferencesummary.GetReferenceSummary;
import ar.lamansys.refcounterref.application.getreference.GetReference;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference.ReferenceGetDto;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference.ReferenceSummaryDto;
import ar.lamansys.refcounterref.infraestructure.input.rest.mapper.GetReferenceMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "Reference", description = "References")
public class ReferenceController {

    private final GetReference getReference;
    private final GetReferenceSummary getreferencesummary;
    private final GetReferenceMapper getReferenceMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public List<ReferenceGetDto> getReference(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId,
            @RequestParam List<Integer> clinicalSpecialtyIds) {
        log.debug("Input parameters -> patientId {}, clinicalSpecialtyIds {}", patientId, clinicalSpecialtyIds);
        List<ReferenceGetDto> result = getReferenceMapper.fromListReferenceGetBo(getReference.run(patientId, clinicalSpecialtyIds));
        log.debug("Output -> result {}", result);
        return result;
    }

	@GetMapping("/requested")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<List<ReferenceSummaryDto>> getReferencesSummary(@PathVariable(name = "institutionId") Integer institutionId,
														  @PathVariable(name = "patientId") Integer patientId,
														  @RequestParam(name = "clinicalSpecialtyId") Integer clinicalSpecialtyId,
														  @RequestParam Integer diaryId) {
		log.debug("Input parameters -> institutionId {}, patientId {}, clinicalSpecialtyId {}, diaryId {}", institutionId, patientId, clinicalSpecialtyId, diaryId);
		List<ReferenceSummaryBo> referenceSummaryBoList = getreferencesummary.run(patientId, clinicalSpecialtyId, diaryId);
		List<ReferenceSummaryDto> result = getReferenceMapper.toReferenceSummaryDtoList(referenceSummaryBoList);
		log.debug("Output -> result {}", result);
		return ResponseEntity.ok().body(result);
	}

}
