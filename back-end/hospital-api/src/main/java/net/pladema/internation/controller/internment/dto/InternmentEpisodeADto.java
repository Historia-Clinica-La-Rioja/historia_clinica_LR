package net.pladema.internation.controller.internment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Integer responsibleDoctorId;
}
