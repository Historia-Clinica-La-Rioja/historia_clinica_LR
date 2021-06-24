package net.pladema.permissions.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class LoggedPersonDto implements Serializable {

    private final String firstName;

    private final String lastName;

    public LoggedPersonDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
