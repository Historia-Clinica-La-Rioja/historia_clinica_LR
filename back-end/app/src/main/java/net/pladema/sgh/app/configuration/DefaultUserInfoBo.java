package net.pladema.sgh.app.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class DefaultUserInfoBo {

    private String password;

    private List<DefaultUserRolBo> roles;

}
