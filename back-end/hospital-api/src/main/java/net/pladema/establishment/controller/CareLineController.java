package net.pladema.establishment.controller;

import ar.lamansys.sgh.shared.infrastructure.input.service.CareLineDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.controller.mapper.CareLineMapper;
import net.pladema.establishment.service.CareLineService;
import net.pladema.establishment.service.domain.CareLineBo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Care lines", description = "Care lines")
@RequestMapping("/institution/{institutionId}/carelines")
public class CareLineController {

    private final CareLineService careLineService;
    private final CareLineMapper careLineMapper;

    @GetMapping()
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<List<CareLineDto>> getAll(@PathVariable(name = "institutionId") Integer institutionId) {
        List<CareLineBo> careLinesBo = careLineService.getCareLines();
        log.debug("Get all care lines  => {}", careLinesBo);
        return ResponseEntity.ok(careLineMapper.toListCareLineDto(careLinesBo));
    }

	@GetMapping(value = "/problems")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<List<CareLineDto>> getByProblemSnomedIdsAndDestinationInstitutionIdWithActiveDiaries(@PathVariable(name = "institutionId") Integer institutionId,
																				   @RequestParam(name = "problemSnomedIds") List<String> problemSnomedIds,
                                                                                   @RequestParam Integer destinationInstitutionId) {
		log.debug("Input parameters -> institutionId {}, problemSnomedIds {}", institutionId, problemSnomedIds);
		List<CareLineBo> careLinesBo = careLineService.getCareLinesByProblemsSctidsAndDestinationInstitutionIdWithActiveDiaries(problemSnomedIds, destinationInstitutionId);
		log.debug("Get care lines by snomedId and institutionId  => {}", careLinesBo);
		return ResponseEntity.ok(careLineMapper.toListCareLineDto(careLinesBo));
	}

	@GetMapping("/attached")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<List<CareLineDto>> getCareLinesAttachedToInstitution(@PathVariable(name = "institutionId") Integer institutionId) {
    	List<CareLineBo> careLinesBo = careLineService.getCareLinesAttachedToInstitution();
		log.debug("Get all care lines with clinical specialties => {}", careLinesBo);
		return ResponseEntity.ok(careLineMapper.toListCareLineDto(careLinesBo));
	}

}