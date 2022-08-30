package net.pladema.staff.domain;

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
public class ProfessionalCompleteBo {

	private Integer id;
	private Integer personId;
    private String firstName;
    private String lastName;
	private String nameSelfDetermination;

	private List<ProfessionBo> professions;
	public ProfessionalCompleteBo(Integer id, Integer personId, String firstName, String lastName, String nameSelfDetermination){
		this.id = id;
		this.personId = personId;
		this.firstName = firstName;
        this.lastName = lastName;
		this.nameSelfDetermination = (nameSelfDetermination == null || nameSelfDetermination.isEmpty() || nameSelfDetermination.isBlank()) ? firstName : nameSelfDetermination;
    }
}
