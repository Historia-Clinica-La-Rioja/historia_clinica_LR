package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.*;
import net.pladema.clinichistory.hospitalization.repository.domain.PatientDischarge;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PatientDischargeBo {

    private Integer internmentEpisodeId;

    private LocalDateTime administrativeDischargeDate;

    private LocalDateTime medicalDischargeDate;

    private Short dischargeTypeId;

	private LocalDateTime physicalDischargeDate;


	public PatientDischargeBo(PatientDischarge patientDischarge) {
        this.internmentEpisodeId = patientDischarge.getInternmentEpisodeId();
        this.administrativeDischargeDate = patientDischarge.getAdministrativeDischargeDate();
        this.medicalDischargeDate = patientDischarge.getMedicalDischargeDate();
        this.dischargeTypeId = patientDischarge.getDischargeTypeId();
		this.physicalDischargeDate = patientDischarge.getPhysicalDischargeDate();
    }

}
