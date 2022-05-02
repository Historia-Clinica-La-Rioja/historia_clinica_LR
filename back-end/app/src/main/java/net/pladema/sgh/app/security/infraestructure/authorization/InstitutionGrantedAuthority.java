package net.pladema.sgh.app.security.infraestructure.authorization;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.permissions.service.RoleAssignmentAuthority;
import net.pladema.permissions.service.dto.RoleAssignment;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
/**
 * Esta clase adapta un RoleAssignment para que cumpla con la
 * interfaz GrantedAuthority. El contexto de seguridad guarda una
 * lista de las autoridades asociadas al usuario logeado.
 *
 */
public class InstitutionGrantedAuthority extends RoleAssignmentAuthority {

	private static final long serialVersionUID = 8208223394015158547L;

	public InstitutionGrantedAuthority(RoleAssignment roleAssignment) {
		super(roleAssignment);
	}
	
}