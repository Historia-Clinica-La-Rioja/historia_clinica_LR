package net.pladema.user.controller.service.domain;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserPersonInfoBo {

    private Integer id;

	private String username;

    private String email;

    private Integer personId;

    private String firstName;

	private String middleNames;

    private String lastName;

	private String otherLastNames;

	private  String nameSelfDetermination;

	private LocalDateTime previousLogin;

    public UserPersonInfoBo(Integer id, String email, Integer personId, String firstName, String middleNames, String lastName, String otherLastNames, String nameSelfDetermination, LocalDateTime previousLogin) {
        this.id = id;
        this.email = email;
        this.personId = personId;
        this.firstName = firstName;
		this.middleNames = middleNames;
        this.lastName = lastName;
		this.otherLastNames = otherLastNames;
		this.nameSelfDetermination = nameSelfDetermination;
		this.previousLogin = previousLogin;
    }

	public String getFullName() {
		String fullName = null;
		if (!(this.lastName == null || this.lastName.isBlank()))
			fullName = this.lastName;
		if (!(this.otherLastNames == null || this.otherLastNames.isBlank()))
			fullName += " " + this.otherLastNames;
		if (AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS.isActive() && !(nameSelfDetermination == null || nameSelfDetermination.isBlank()))
			fullName = this.nameSelfDetermination + " " + fullName;
		else {
			if (!(this.middleNames == null || this.middleNames.isBlank()))
				fullName = this.middleNames + " " + fullName;
			if (!(this.firstName == null || this.firstName.isBlank()))
				fullName = this.firstName + " " + fullName;
		}
		return fullName;
	}

}
