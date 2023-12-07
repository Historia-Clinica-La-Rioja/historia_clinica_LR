package net.pladema.establishment.application.practices;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.diary.service.DiaryPracticeService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetPracticesByActiveDiaries {

	private final DiaryPracticeService diaryPracticeService;

	public List<SharedSnomedDto> run(Integer institutionId) {
		log.debug("Input parameter -> institutionId {}", institutionId);
		List<SharedSnomedDto> result = diaryPracticeService.getPracticesByActiveDiaries(institutionId).stream()
				.map(bo -> 	new SharedSnomedDto(bo.getId(), bo.getSctid(), bo.getPt()))
				.collect(Collectors.toList());;
		log.debug("Output Get Active Diaries Practices {}", result);
		return result;
	}
}
