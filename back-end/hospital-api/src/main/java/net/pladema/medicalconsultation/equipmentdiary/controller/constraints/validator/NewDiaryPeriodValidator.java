package net.pladema.medicalconsultation.equipmentdiary.controller.constraints.validator;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.NewDiaryPeriodValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryADto;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmetDiaryService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class NewDiaryPeriodValidator extends AbstractDiaryPeriodValidator<NewDiaryPeriodValid, EquipmentDiaryADto> {

	private final EquipmetDiaryService equipmetDiaryService;

	public NewDiaryPeriodValidator(LocalDateMapper localDateMapper, EquipmetDiaryService equipmetDiaryService) {
		super(localDateMapper);
		this.equipmetDiaryService = equipmetDiaryService;
	}

	@Override
	protected List<Integer> getOverlappingDiary(EquipmentDiaryADto equipmentDiary,
												LocalDate startDate, LocalDate endDate) {
		return equipmetDiaryService.getAllOverlappingEquipmentDiaryByEquipment(equipmentDiary.getEquipmentId(), startDate, endDate, equipmentDiary.getAppointmentDuration(), Optional.empty());
	}

}
