package net.pladema.user.controller.dto;

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

	private String nameSelfDetermination;

    public UserPersonDto(Integer id, String firstName, String lastName, String nameSelfDetermination) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
		this.nameSelfDetermination = nameSelfDetermination;
    }
}
