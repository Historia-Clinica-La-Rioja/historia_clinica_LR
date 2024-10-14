package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.application.getreferencesummary.GetReferenceSummary;
import ar.lamansys.refcounterref.application.getreference.GetReference;
import ar.lamansys.refcounterref.application.modifyreferencebymanagerrole.ModifyReferenceByManagerRole;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference.ReferenceDataDto;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/institutions/{institutionId}/reference")
@Tag(name = "Reference", description = "References")
public class ReferenceController {

    private final GetReference getReference;
    private final GetReferenceSummary getReferenceSummary;
    private final GetReferenceMapper getReferenceMapper;
	private final ModifyReferenceByManagerRole modifyReferenceByManagerRole;

    @GetMapping("/patient/{patientId}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
    public List<ReferenceDataDto> getReferences(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId,
            @RequestParam List<Integer> clinicalSpecialtyIds) {
        log.debug("Input parameters -> patientId {}, clinicalSpecialtyIds {}", patientId, clinicalSpecialtyIds);
        List<ReferenceDataDto> result = getReferenceMapper.fromListReferenceDataBo(getReference.run(institutionId, patientId, clinicalSpecialtyIds));
        log.debug("Output -> result {}", result);
        return result;
    }

	@GetMapping("/patient/{patientId}/requested")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA') || hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_LOCAL', 'GESTOR_DE_ACCESO_REGIONAL')")
	public ResponseEntity<List<ReferenceSummaryDto>> getReferencesSummary(@PathVariable(name = "institutionId") Integer institutionId,
																		  @PathVariable(name = "patientId") Integer patientId,
																		  @RequestParam(value="clinicalSpecialtyId" , required = false) Integer clinicalSpecialtyId,
																		  @RequestParam(name = "careLineId", required = false) Integer careLineId,
																		  @RequestParam(value = "practiceId", required = false) Integer practiceId) {
		log.debug("Input parameters -> institutionId {}, patientId {}, clinicalSpecialtyId {}, careLineId {}", institutionId, patientId, clinicalSpecialtyId, careLineId);
		List<ReferenceSummaryBo> referenceSummaryBoList = getReferenceSummary.run(institutionId, patientId, clinicalSpecialtyId, careLineId, practiceId);
		referenceSummaryBoList = referenceSummaryBoList.stream()
				.filter(r -> r.getRegulationState().equals(EReferenceRegulationState.APPROVED))
				.collect(Collectors.toList());
		List<ReferenceSummaryDto> result = getReferenceMapper.toReferenceSummaryDtoList(referenceSummaryBoList);
		log.debug("Output -> result {}", result);
		return ResponseEntity.ok().body(result);
	}

	@PutMapping("/{referenceId}/update-by-manager")
	@PreAuthorize("hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public boolean updateReferenceByManagerRole(@PathVariable(name = "institutionId") Integer institutionId,
												@PathVariable(name = "referenceId") Integer referenceId,
												@RequestParam(name = "destinationInstitutionId", required = false) Integer destinationInstitutionId,
												@RequestParam(value = "fileIds", required = false) List<Integer> fileIds) {
		log.debug("Input parameters -> institutionId {}, referenceId {}, destinationInstitutionId {}, fileIds {}", institutionId, referenceId, destinationInstitutionId, fileIds);
		modifyReferenceByManagerRole.run(referenceId, destinationInstitutionId, fileIds);
		return Boolean.TRUE;
	}

}
