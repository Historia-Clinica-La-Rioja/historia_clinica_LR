package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponsibleDoctorVo {

    private Integer id;

    private String firstName;

    private String lastName;

    private List<String> licenses;

	private String nameSelfDetermination;
}
