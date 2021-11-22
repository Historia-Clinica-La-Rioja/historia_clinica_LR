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

    public UserPersonDto(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
