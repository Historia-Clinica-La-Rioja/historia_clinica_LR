package net.pladema.sgh.app.security.infraestructure.authorization;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.permissions.service.dto.RoleAssignment;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
/**
 * Esta clase adapta un RoleAssignment para que cumpla con la
 * interfaz GrantedAuthority. El contexto de seguridad guarda una
 * lista de las autoridades asociadas al usuario logeado.
 *
 */
public class InstitutionGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 8208223394015158547L;

	@EqualsAndHashCode.Include 
	private RoleAssignment roleAssignment;

	public InstitutionGrantedAuthority(RoleAssignment roleAssignment) {
		super();
		this.roleAssignment = roleAssignment;
	}

	@Override
	public String getAuthority() {
		return this.roleAssignment.getRole().getValue();
	}
	
}