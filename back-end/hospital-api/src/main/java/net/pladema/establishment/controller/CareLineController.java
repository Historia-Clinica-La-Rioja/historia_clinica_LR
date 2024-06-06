package net.pladema.establishment.controller;

import ar.lamansys.sgh.shared.infrastructure.input.service.CareLineDto;
import ar.lamansys.sgx.shared.security.UserInfo;
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
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO') || hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
    public ResponseEntity<List<CareLineDto>> getCareLines(@PathVariable(name = "institutionId") Integer institutionId) {
        List<CareLineBo> careLinesBo = careLineService.getCareLines();
        log.debug("Get all care lines with specialties => {}", careLinesBo);
        return ResponseEntity.ok(careLineMapper.toListCareLineDto(careLinesBo));
    }

	@GetMapping("/all")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ABORDAJE_VIOLENCIAS') || hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public ResponseEntity<List<CareLineDto>> getAllCareLines(@PathVariable(name = "institutionId") Integer institutionId) {
		List<CareLineBo> careLinesBo = careLineService.getAllCareLines();
		log.debug("Get all domain care lines including confidential  => {}", careLinesBo);
		List<CareLineDto> result = careLineMapper.toListCareLineDto(careLinesBo);
		return ResponseEntity.ok(result);
	}

	@GetMapping(value = "/by-problems")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<List<CareLineDto>> getAllByProblems(@PathVariable(name = "institutionId") Integer institutionId,
															  @RequestParam(name = "snomedSctids") List<String> snomedSctids) {
		log.debug("Input parameters -> institutionId {}, snomedSctids {}", institutionId, snomedSctids);
		List<CareLineBo> careLinesBo = careLineService.getAllByProblems(snomedSctids);
		log.debug("Get care lines by problems (snomed sctids) {}", careLinesBo);
		return ResponseEntity.ok(careLineMapper.toListCareLineDto(careLinesBo));
	}

	@GetMapping("/attached")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, VIRTUAL_CONSULTATION_PROFESSIONAL, VIRTUAL_CONSULTATION_RESPONSIBLE')")
	public ResponseEntity<List<CareLineDto>> getCareLinesAttachedToInstitutions(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		Integer loggedUserId = UserInfo.getCurrentAuditor();
    	List<CareLineBo> careLinesBo = careLineService.getCareLinesAttachedToInstitutions(institutionId, loggedUserId);
		log.debug("Get all care lines with clinical specialties => {}", careLinesBo);
		return ResponseEntity.ok(careLineMapper.toListCareLineDto(careLinesBo));
	}

	@GetMapping("/attached-to-institution")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, VIRTUAL_CONSULTATION_PROFESSIONAL, VIRTUAL_CONSULTATION_RESPONSIBLE')")
	public ResponseEntity<List<CareLineDto>> getCareLinesAttachedToInstitution(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<CareLineBo> careLinesBo = careLineService.getCareLinesAttachedToInstitution(institutionId);
		log.debug("Get all care lines with clinical specialties => {}", careLinesBo);
		return ResponseEntity.ok(careLineMapper.toListCareLineDto(careLinesBo));
	}

	@GetMapping("/virtual-consultation")
	public List<CareLineDto> getVirtualConsultationCareLinesByInstitutionId(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<CareLineDto> result = careLineMapper.toListCareLineDto(careLineService.getVirtualConsultationCareLinesByInstitutionId(institutionId));
		log.debug("Output -> {}", result);
		return result;
	}

}