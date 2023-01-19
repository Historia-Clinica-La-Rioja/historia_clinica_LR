package net.pladema.medicalconsultation.equipmentdiary.controller.constraints.validator;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.NewDiaryPeriodValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryADto;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class NewEquipmentDiaryPeriodValidator extends AbstractEquipmentDiaryPeriodValidator<NewDiaryPeriodValid, EquipmentDiaryADto> {

	private final EquipmentDiaryService equipmentDiaryService;

	public NewEquipmentDiaryPeriodValidator(LocalDateMapper localDateMapper, EquipmentDiaryService equipmentDiaryService) {
		super(localDateMapper);
		this.equipmentDiaryService = equipmentDiaryService;
	}

	@Override
	protected List<Integer> getOverlappingDiary(EquipmentDiaryADto equipmentDiary,
												LocalDate startDate, LocalDate endDate) {
		return equipmentDiaryService.getAllOverlappingEquipmentDiaryByEquipment(equipmentDiary.getEquipmentId(), startDate, endDate, equipmentDiary.getAppointmentDuration(), Optional.empty());
	}

}
