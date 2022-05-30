package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponsibleDoctorVo {

    private Integer id;

    private String firstName;

    private String lastName;

    private String licence;

	private String nameSelfDetermination;
}
