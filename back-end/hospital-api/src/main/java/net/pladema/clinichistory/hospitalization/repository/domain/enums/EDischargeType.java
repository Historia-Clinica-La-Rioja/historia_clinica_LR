package net.pladema.clinichistory.hospitalization.repository.domain.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum EDischargeType {

	MEDICAL_DISCHARGE(1, "Alta médica", true, true),
	TRANSFER_TO_ANOTHER_FACILITY(2, "Traslado a otro establecimiento", true, false),
	DECEASE(3, "Defunción", true, true),
	VOLUNTARY_LEAVE(4, "Retiro voluntario", true, true),
	OTHER(5, "Otro", true, true),
	CANCELLATION(6, "Cancelación", false, true),
	SUSPENSION(7, "Suspensión", false, true),
	TRANSFER_TO_ANOTHER_HOSPITAL(8, "Traslado a otro hospital", false, true),
	TRANSFER_TO_THIRD_LEVEL(9, "Traslado a tercer nivel", false, true),
	TRANSFER_TO_ANOTHER_TYPE_OF_INSTITUTION(10, "Traslado a otro tipo de institución", false, true),
	DISCHARGE_TO_HOSPITALIZATION(11, "Alta a internación", false, true),
	DISCHARGE_TO_HOME_HOSPITALIZATION(12, "Alta a internación domiciliaria", false, true),
	DISCHARGE_TO_HOME_FOLLOW_UP(13, "Alta a seguimiento domiciliario", false, true),
	LEAVE_WITHOUT_MEDICAL_DISCHARGE(14, "Retiro sin alta médica (fuga)", false, true),
	HOSPITAL_DISCHARGE(15, "Alta hospitalaria", false, true);

	private final Short id;
	private final String description;
	private final Boolean internment;
	private final Boolean emergencyCare;

	EDischargeType(Number id, String description, Boolean internment, Boolean emergencyCare){
		this.id = id.shortValue();
		this.description = description;
		this.internment = internment;
		this.emergencyCare = emergencyCare;
	}

	public static List<EDischargeType> getAllDoctorEmergencyCareDischargeTypes(){
		return List.of(EDischargeType.MEDICAL_DISCHARGE, EDischargeType.DISCHARGE_TO_HOSPITALIZATION,
				EDischargeType.DISCHARGE_TO_HOME_HOSPITALIZATION, EDischargeType.DISCHARGE_TO_HOME_FOLLOW_UP,
				EDischargeType.TRANSFER_TO_ANOTHER_HOSPITAL, EDischargeType.TRANSFER_TO_ANOTHER_TYPE_OF_INSTITUTION,
				EDischargeType.TRANSFER_TO_THIRD_LEVEL, EDischargeType.DECEASE, EDischargeType.VOLUNTARY_LEAVE,
				EDischargeType.LEAVE_WITHOUT_MEDICAL_DISCHARGE, EDischargeType.CANCELLATION,
				EDischargeType.SUSPENSION, EDischargeType.OTHER);
	}

	public static List<EDischargeType> getAllNurseEmergencyCareDischargeTypes(){
		return List.of(EDischargeType.HOSPITAL_DISCHARGE, EDischargeType.VOLUNTARY_LEAVE, EDischargeType.LEAVE_WITHOUT_MEDICAL_DISCHARGE);
	}

}
