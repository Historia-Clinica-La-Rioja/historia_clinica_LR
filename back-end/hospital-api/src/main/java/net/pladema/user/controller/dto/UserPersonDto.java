package net.pladema.user.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserPersonDto implements Serializable {

    private String firstName;

    private String lastName;
}
