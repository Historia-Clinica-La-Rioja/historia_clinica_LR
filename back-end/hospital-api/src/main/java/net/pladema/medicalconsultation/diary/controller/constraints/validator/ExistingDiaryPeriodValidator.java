package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import net.pladema.medicalconsultation.diary.controller.constraints.ExistingDiaryPeriodValid;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryDto;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

public class ExistingDiaryPeriodValidator extends AbstractDiaryPeriodValidator<ExistingDiaryPeriodValid, DiaryDto> {

	private final DiaryService diaryService;

	public ExistingDiaryPeriodValidator(LocalDateMapper localDateMapper, DiaryService diaryService) {
		super(localDateMapper);
		this.diaryService = diaryService;
	}

	@Override
	protected List<Integer> getOverlappingDiary(DiaryDto diary, LocalDate startDate, LocalDate endDate) {
		return diaryService.getAllOverlappingDiaryByProfessional(diary.getHealthcareProfessionalId(), diary.getDoctorsOfficeId(),
				startDate, endDate, diary.getAppointmentDuration(), Optional.of(diary.getId()));
	}

}
