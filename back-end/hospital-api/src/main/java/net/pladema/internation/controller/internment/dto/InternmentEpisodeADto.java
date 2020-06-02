package net.pladema.internation.controller.internment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.featureflags.controller.constraints.SGHNotNull;

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

    @SGHNotNull(message = "{internment.responsible.doctor.required}", ffs = {"medicoResponsableRequerido"})
    private Integer responsibleDoctorId;

    private ResponsibleContactDto responsibleContact;
}
