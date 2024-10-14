package net.pladema.medicalconsultation.diary.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.diary.repository.DiaryPracticeRepository;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryPractice;
import net.pladema.medicalconsultation.diary.service.DiaryPracticeService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiaryPracticeServiceImpl implements DiaryPracticeService {

	private final DiaryPracticeRepository diaryPracticeRepository;

	@Override
	@Transactional
	public void updateDiaryPractices(List<Integer> practicesId, Integer diaryId) {
		List<DiaryPractice> diaryPracticesHistory = diaryPracticeRepository.findAllByDiaryId(diaryId);

		diaryPracticesHistory.stream()
				.filter(diaryPractice -> practicesId.stream().noneMatch(diaryPractice.getSnomedId()::equals))
				.peek(dp -> {
					log.debug("Disassociate practice to diary, practice id: ", dp.getSnomedId());
					diaryPracticeRepository.delete(dp);
		}).collect(toList());

		practicesId.forEach(pId -> diaryPracticesHistory.stream().filter(dph ->
				dph.getSnomedId().equals(pId)).findFirst().ifPresentOrElse(dp -> {
					if (dp.isDeleted()) {
						log.debug("Update associate practice to diary, practice id: ", dp.getSnomedId());
						dp.setDeleted(false);
						dp.setDeletedBy(null);
						dp.setDeletedOn(null);
						diaryPracticeRepository.save(dp);
					}
				}, () -> addDiaryPractice(pId, diaryId)
		));
	}

	@Override
	public List<SnomedBo> getAllByDiaryId(Integer diaryId) {
		log.debug("Input parameter -> diaryId {}", diaryId);
		List<SnomedBo> practices = diaryPracticeRepository.getByDiaryId(diaryId);
		log.trace("Output -> {}", practices);
		return practices;
	}

	@Override
	public List<SnomedBo> getPracticesByActiveDiaries(Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		return diaryPracticeRepository.getActiveDiariesPractices(institutionId);
	}

	@Override
	public boolean hasPractice(Integer diaryId) {
		log.debug("Input parameters -> diaryId {},", diaryId);
		return diaryPracticeRepository.hasPractices(diaryId);
	}

	private void addDiaryPractice(Integer practiceId, Integer diaryId) {
		log.debug("Save associate practice to diary, practice id: ", practiceId);
		DiaryPractice diaryPractice = new DiaryPractice();
		diaryPractice.setDiaryId(diaryId);
		diaryPractice.setSnomedId(practiceId);
		diaryPracticeRepository.save(diaryPractice);
	}
}
