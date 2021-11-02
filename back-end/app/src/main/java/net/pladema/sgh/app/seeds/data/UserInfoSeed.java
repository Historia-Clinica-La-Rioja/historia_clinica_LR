package net.pladema.sgh.app.seeds.data;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserInfoSeed {

    private String username;

    private String password;

    private boolean enabled;

    private PersonInfoSeed person;

    private List<UserRoleSeed> roles = new ArrayList<>();

    private List<HealthcareProfessionalInfoSeed> healthcareProfessionals = new ArrayList<>();

}
