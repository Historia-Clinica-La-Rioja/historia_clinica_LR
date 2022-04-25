package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDataVo {

	private Integer userId;

    private String firstName;

    private String lastName;

	private String nameSelfDetermination;
}
