package net.pladema.clinichistory.hospitalization.service.domain;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InternmentEpisodeProcessBo {

    private Integer id;

    private boolean inProgress;

	private boolean patientHospitalized;

}
