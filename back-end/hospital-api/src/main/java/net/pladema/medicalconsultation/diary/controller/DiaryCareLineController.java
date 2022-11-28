package net.pladema.medicalconsultation.diary.controller;

import ar.lamansys.sgh.shared.infrastructure.input.service.CareLineDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.controller.mapper.CareLineMapper;
import net.pladema.establishment.service.domain.CareLineBo;

import net.pladema.medicalconsultation.diary.service.DiaryCareLineService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Diary Care Lines", description = "Diary Care Lines")
@RequestMapping("/institution/{institutionId}/diary-care-lines")
public class DiaryCareLineController {

	private final DiaryCareLineService diaryCareLineService;

	private final CareLineMapper careLineMapper;

	@GetMapping("/{clinicalSpecialtyId}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<List<CareLineDto>> getPossibleCareLinesForDiary(@PathVariable(name = "institutionId") Integer institutionId,
																		  @PathVariable(name = "clinicalSpecialtyId") Integer clinicalSpecialtyId) {
		log.debug("Input parameters -> institutionId {}, clinicalSpecialtyId {} ", institutionId, clinicalSpecialtyId);
		List<CareLineBo> careLinesBo = diaryCareLineService.getPossibleCareLinesForDiary(institutionId, clinicalSpecialtyId);
		log.debug("Get all care lines  => {}", careLinesBo);
		return ResponseEntity.ok(careLineMapper.toListCareLineDto(careLinesBo));
	}
}
