package net.pladema.user.controller.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserPersonInfoBo {

    private Integer id;

    private String email;

    private Integer personId;

    private String firstName;

    private String lastName;

	private  String nameSelfDetermination;

	private LocalDateTime previousLogin;

    public UserPersonInfoBo(Integer id, String email, Integer personId, String firstName, String lastName, String nameSelfDetermination, LocalDateTime previousLogin) {
        this.id = id;
        this.email = email;
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
		this.nameSelfDetermination = nameSelfDetermination;
		this.previousLogin = previousLogin;
    }
}
