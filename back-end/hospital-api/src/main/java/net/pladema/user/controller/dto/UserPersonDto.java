package net.pladema.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@Setter
public class UserPersonDto implements Serializable {

    @Nullable
    private Integer id;

    private String firstName;

    private String lastName;

	private String middleNames;

	private String othersLastNames;

	private String nameSelfDetermination;

    public UserPersonDto(Integer id, String firstName, String lastName, String nameSelfDetermination) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
		this.nameSelfDetermination = nameSelfDetermination;
    }

	public UserPersonDto(String firstName, String lastName, String middleNames, String othersLastNames, String nameSelfDetermination) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleNames = middleNames;
		this.othersLastNames = othersLastNames;
		this.nameSelfDetermination = nameSelfDetermination;
	}
}
