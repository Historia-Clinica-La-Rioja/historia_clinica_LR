package net.pladema.emergencycare.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosisDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthConditionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.ReferableItemDto;
import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientAllergyConditionDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientAnthropometricDataDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientFamilyHistoryDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientMedicationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientProcedureDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientReasonDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientRiskFactorDto;

import java.util.List;

@Getter
@Setter
public class EmergencyCareEvolutionNoteClinicalData {

	private HealthConditionDto mainDiagnosis;

	private List<DiagnosisDto> diagnosis;

	private List<OutpatientReasonDto> reasons;

	private String evolutionNote;

	private OutpatientAnthropometricDataDto anthropometricData;

	private OutpatientRiskFactorDto riskFactors;

	private ReferableItemDto<OutpatientFamilyHistoryDto> familyHistories;

	private List<OutpatientMedicationDto> medications;

	private ReferableItemDto<OutpatientAllergyConditionDto> allergies;

	private List<OutpatientProcedureDto> procedures;

}
