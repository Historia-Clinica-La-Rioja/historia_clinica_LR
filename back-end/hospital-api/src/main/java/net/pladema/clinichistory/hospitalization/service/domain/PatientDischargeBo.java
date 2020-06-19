package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.*;
import net.pladema.clinichistory.hospitalization.repository.domain.PatientDischarge;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PatientDischargeBo {

    private Integer internmentEpisodeId;

    private LocalDate administrativeDischargeDate;

    private LocalDate medicalDischargeDate;

    private Short dischargeTypeId;

    public PatientDischargeBo(PatientDischarge patientDischarge) {
        this.internmentEpisodeId = patientDischarge.getInternmentEpisodeId();
        this.administrativeDischargeDate = patientDischarge.getAdministrativeDischargeDate();
        this.medicalDischargeDate = patientDischarge.getMedicalDischargeDate();
        this.dischargeTypeId = patientDischarge.getDischargeTypeId();
    }
}
