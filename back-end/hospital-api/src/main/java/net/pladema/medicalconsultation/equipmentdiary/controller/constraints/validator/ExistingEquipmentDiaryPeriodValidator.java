package net.pladema.medicalconsultation.equipmentdiary.controller.constraints.validator;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryDto;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.ExistingEquipmentDiaryPeriodValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryDto;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ExistingEquipmentDiaryPeriodValidator extends AbstractEquipmentDiaryPeriodValidator<ExistingEquipmentDiaryPeriodValid, EquipmentDiaryDto> {
	private final EquipmentDiaryService equipmentDiaryService;

	public ExistingEquipmentDiaryPeriodValidator(LocalDateMapper localDateMapper, EquipmentDiaryService equipmentDiaryService) {
		super(localDateMapper);
		this.equipmentDiaryService = equipmentDiaryService;
	}

	@Override
	protected List<Integer> getOverlappingDiary(EquipmentDiaryDto diary, LocalDate startDate, LocalDate endDate) {
		return equipmentDiaryService.getAllOverlappingEquipmentDiaryByEquipment(diary.getEquipmentId(), startDate,
				endDate, diary.getAppointmentDuration(), Optional.of(diary.getId()));
	}
}
