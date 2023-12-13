package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import net.pladema.medicalconsultation.diary.controller.constraints.ExistingDiaryPeriodValid;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

public class ExistingDiaryPeriodValidator extends DiaryPeriodValidator<ExistingDiaryPeriodValid, DiaryDto> {

	public ExistingDiaryPeriodValidator(LocalDateMapper localDateMapper) {
		super(localDateMapper);
	}

}
