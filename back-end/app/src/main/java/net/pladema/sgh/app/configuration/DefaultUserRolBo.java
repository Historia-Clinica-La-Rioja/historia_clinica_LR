package net.pladema.sgh.app.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pladema.permissions.repository.enums.ERole;

@AllArgsConstructor
@Getter
public class DefaultUserRolBo {

    private ERole rol;

    private Integer institutionId;

}
