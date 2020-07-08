package net.pladema.permissions.service.domain;

import lombok.Getter;
import net.pladema.user.repository.entity.User;

@Getter
public class UserBo {

    private String email;

    private Integer id;

    private Integer personId;

    public UserBo (User user){
        this.id = user.getId();
        this.email = user.getUsername();
        this.personId = user.getPersonId();
    }
}
