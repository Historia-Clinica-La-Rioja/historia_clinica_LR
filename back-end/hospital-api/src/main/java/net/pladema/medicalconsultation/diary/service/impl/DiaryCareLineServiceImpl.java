package net.pladema.medicalconsultation.diary.service.impl;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDiaryCareLinePort;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.service.CareLineService;
import net.pladema.establishment.service.domain.CareLineBo;
import net.pladema.medicalconsultation.diary.repository.DiaryCareLineRepository;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryCareLine;
import net.pladema.medicalconsultation.diary.service.DiaryCareLineService;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiaryCareLineServiceImpl implements DiaryCareLineService, SharedDiaryCareLinePort {

	private final DiaryCareLineRepository diaryCareLineRepository;

	private final CareLineService careLineService;

	@Override
	public void updateCareLinesAssociatedToDiary(Integer diaryId, List<Integer> careLines) {

		List<DiaryCareLine> diaryCareLinesHistory = this.diaryCareLineRepository.findAllByDiaryId(diaryId);

		diaryCareLinesHistory.stream().filter(careLine -> careLines.stream().noneMatch(careLine.getPk().getCareLineId()::equals)).peek(cl -> {
			log.debug("Disassociate care line to diary, care line id: ", cl.getId());
			this.diaryCareLineRepository.delete(cl);
		}).collect(toList());

		careLines.forEach(clId -> diaryCareLinesHistory.stream().filter(cl -> cl.getPk().getCareLineId().equals(clId)).findFirst().ifPresentOrElse(cl -> {
			if (cl.isDeleted()) {
				log.debug("Save associate care line to diary, care line id: ", clId);
				cl.setDeleted(false);
				cl.setDeletedBy(null);
				cl.setDeletedOn(null);
				this.diaryCareLineRepository.save(cl);
			}
		}, () -> {
			log.debug("Save associate care line to diary, care line id: ", clId);
			this.diaryCareLineRepository.save(new DiaryCareLine(diaryId, clId));
		}));
	}

	@Override
	public List<CareLineBo> getAllCareLinesByDiaryId(Integer diaryId, Integer healthcareProfessionalId) {
		log.debug("Input parameter -> diaryId {}", diaryId);
		List<CareLineBo> careLines = diaryCareLineRepository.getCareLinesByDiaryId(diaryId);
		log.trace("Output -> {}", careLines);
		return careLines;
	}

	@Override
	public List<CareLineBo> getPossibleCareLinesForDiary(Integer institutionId, Integer clinicalSpecialtyId) {
		log.debug("Input parameters -> institutionId {}, clinicalSpecialtyId {}", institutionId, clinicalSpecialtyId);
		List<CareLineBo> careLines = careLineService.getCareLinesByClinicalSpecialtyAndInstitutionId(institutionId, clinicalSpecialtyId);
		log.trace("Output -> {}", careLines);
		return careLines;
	}

	@Override
	public List<Integer> getCareLineIdsByDiaryId(Integer diaryId) {
		log.debug("Input parameters -> diaryId {}", diaryId);
		List<Integer> result = diaryCareLineRepository.getCareLineIdsByDiary(diaryId);
		log.trace("Output -> {}", result);
		return result;
	}
}
