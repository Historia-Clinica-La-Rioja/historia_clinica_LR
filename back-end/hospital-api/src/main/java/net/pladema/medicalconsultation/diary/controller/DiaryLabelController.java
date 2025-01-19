package net.pladema.medicalconsultation.diary.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.diary.controller.dto.DiaryLabelDto;

import net.pladema.medicalconsultation.diary.service.FetchDiaryLabel;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/institutions/{institutionId}/medicalConsultations/diary/{diaryId}")
public class DiaryLabelController {

	private final FetchDiaryLabel fetchDiaryLabel;

	public static final String OUTPUT = "Output -> {}";

	@GetMapping("/labels")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_AGENDA, ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<List<DiaryLabelDto>> getDiaryLabels(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "diaryId") Integer diaryId) {
		log.debug("Input parameters -> institutionId {}, diaryId {}", institutionId, diaryId);
		List<DiaryLabelDto> result = fetchDiaryLabel.run(diaryId)
				.stream()
				.map(DiaryLabelDto::new)
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}
}
