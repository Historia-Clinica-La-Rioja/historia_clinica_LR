package ar.lamansys.immunization.domain.user;


import lombok.Getter;

@Getter
public class RolePermissionException extends RuntimeException {

    public final RolesExceptionEnum code;

    public RolePermissionException(RolesExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }
}
