package net.pladema.clinichistory.hospitalization.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.featureflags.controller.constraints.SGHNotNull;

import static ar.lamansys.sgx.shared.featureflags.AppFeature.RESPONSIBLE_DOCTOR_REQUIRED;

@Getter
@Setter
@ToString
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
