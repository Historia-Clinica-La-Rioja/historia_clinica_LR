package net.pladema.user.controller.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPersonInfoBo {

    private Integer id;

    private String email;

    private Integer personId;

    private String firstName;

    private String lastName;

    public UserPersonInfoBo(Integer id, String email, Integer personId, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
