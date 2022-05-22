package net.pladema.clinichistory.hospitalization.controller.dto;

import static ar.lamansys.sgx.shared.featureflags.AppFeature.RESPONSIBLE_DOCTOR_REQUIRED;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.featureflags.controller.constraints.SGHNotNull;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InternmentEpisodeADto {

    private Integer patientId;

    private Integer bedId;

    private Long noteId;

    private LocalDateTime entryDate;

    private LocalDate dischargeDate;

    private Integer institutionId;

    private Integer patientMedicalCoverageId;

    @SGHNotNull(message = "{internment.responsible.doctor.required}", ffs = {RESPONSIBLE_DOCTOR_REQUIRED})
    private Integer responsibleDoctorId;

    @Nullable
    private ResponsibleContactDto responsibleContact;
}
