package net.pladema.clinichistory.hospitalization.controller.dto;

import static ar.lamansys.sgx.shared.featureflags.AppFeature.RESPONSIBLE_DOCTOR_REQUIRED;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.featureflags.controller.constraints.SGHNotNull;

import javax.annotation.Nullable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class InternmentEpisodeADto {

    private Integer patientId;

    private Integer bedId;

    private Integer clinicalSpecialtyId;

    private Long noteId;

    private LocalDate entryDate;

    private LocalDate dischargeDate;

    private Integer institutionId;

    @SGHNotNull(message = "{internment.responsible.doctor.required}", ffs = {RESPONSIBLE_DOCTOR_REQUIRED})
    private Integer responsibleDoctorId;

    @Nullable
    private ResponsibleContactDto responsibleContact;
}
