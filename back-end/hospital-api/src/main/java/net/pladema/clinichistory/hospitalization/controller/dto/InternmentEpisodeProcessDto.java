package net.pladema.clinichistory.hospitalization.controller.dto;


import lombok.*;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InternmentEpisodeProcessDto {

    @Nullable
    private Integer id;

    private boolean inProgress = false;

 	private boolean patientHospitalized = false;

}
