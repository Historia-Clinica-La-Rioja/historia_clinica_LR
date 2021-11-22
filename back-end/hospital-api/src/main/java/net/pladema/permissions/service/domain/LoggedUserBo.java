package net.pladema.permissions.service.domain;

import lombok.Getter;

@Getter
public class LoggedUserBo {

    private String email;

    private Integer id;

    private Integer personId;

    private String firstName;

    private String lastName;

    public LoggedUserBo(String email, Integer id, Integer personId, String firstName, String lastName) {
        this.email = email;
        this.id = id;
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
