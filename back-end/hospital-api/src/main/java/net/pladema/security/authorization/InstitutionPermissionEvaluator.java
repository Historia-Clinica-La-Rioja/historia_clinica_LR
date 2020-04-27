package net.pladema.security.authorization;

import java.io.Serializable;
import java.util.List;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import net.pladema.permissions.service.dto.RoleAssignment;

/**
 * Implementa hasPermission que se puede acceder desde las anotaciones
 * de seguridad del estilo @PreAuthorize("hasPermission(....)").
 */
public class InstitutionPermissionEvaluator implements PermissionEvaluator {
    
	@Override
	/**
	 * targetDomainObject: id de la institución
	 * permission: tiene que ser uno de los definidos en ERole
	 */
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
    	return hasPermissionInInstitution(auth, (Integer) targetDomainObject, (String) permission);
    }

    @Override
    /**
     * targetId: id de la institución
     * targetType: por ahora tiene que ser "Institution"
     * permission: tiene que ser uno de los definidos en ERole 
     */
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
    	if (!targetType.equals("Institution")) return false;
    	return hasPermissionInInstitution(auth, (Integer) targetId, (String) permission);
    }

	private boolean hasPermissionInInstitution(Authentication auth, Integer targetId, String permission) {
		List<InstitutionGrantedAuthority> authorities = 
				(List<InstitutionGrantedAuthority>) auth.getAuthorities();
		RoleAssignment requestedRole = new RoleAssignment(permission, targetId);
		return authorities.contains(new InstitutionGrantedAuthority(requestedRole));
	}
	
}
