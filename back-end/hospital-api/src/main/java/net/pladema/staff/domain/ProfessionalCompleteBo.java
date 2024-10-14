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
	private String middleNames;
    private String lastName;
	private String nameSelfDetermination;
	private String otherLastNames;
	private List<ProfessionBo> professions;

	public ProfessionalCompleteBo(Integer id, Integer personId, String firstName, String middleNames, String lastName, String nameSelfDetermination, String otherLastNames){
		this.id = id;
		this.personId = personId;
		this.firstName = firstName;
		this.middleNames = middleNames;
        this.lastName = lastName;
		this.nameSelfDetermination = (nameSelfDetermination == null || nameSelfDetermination.isEmpty() || nameSelfDetermination.isBlank()) ? firstName : nameSelfDetermination;
		this.otherLastNames = otherLastNames;
    }

	public String getCompleteName(String name){
		return String.format("%s %s", name, lastName);
	}

}
