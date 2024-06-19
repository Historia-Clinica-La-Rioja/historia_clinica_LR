package net.pladema.clinichistory.hospitalization.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private Integer responsibleDoctorId;

    @Nullable
    private ResponsibleContactDto responsibleContact;
}
