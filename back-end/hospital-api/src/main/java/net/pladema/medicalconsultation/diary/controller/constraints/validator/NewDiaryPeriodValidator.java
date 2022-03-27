package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import net.pladema.medicalconsultation.diary.controller.constraints.NewDiaryPeriodValid;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

public class NewDiaryPeriodValidator extends AbstractDiaryPeriodValidator<NewDiaryPeriodValid, DiaryADto> {

	private final DiaryService diaryService;

	public NewDiaryPeriodValidator(LocalDateMapper localDateMapper, DiaryService diaryService) {
		super(localDateMapper);
		this.diaryService = diaryService;
	}

	@Override
	protected List<Integer> getOverlappingDiary(DiaryADto diary,
			LocalDate startDate, LocalDate endDate) {
		return diaryService.getAllOverlappingDiaryByProfessional(diary.getHealthcareProfessionalId(), diary.getDoctorsOfficeId(), startDate, endDate, diary.getAppointmentDuration(), Optional.empty());
	}

}
