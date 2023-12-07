package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import net.pladema.medicalconsultation.diary.controller.constraints.NewDiaryPeriodValid;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

public class NewDiaryPeriodValidator extends DiaryPeriodValidator<NewDiaryPeriodValid, DiaryADto> {

	public NewDiaryPeriodValidator(LocalDateMapper localDateMapper) {
		super(localDateMapper);
	}

}
