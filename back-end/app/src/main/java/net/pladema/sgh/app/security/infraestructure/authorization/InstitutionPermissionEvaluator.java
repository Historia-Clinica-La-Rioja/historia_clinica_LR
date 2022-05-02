package net.pladema.sgh.app.security.infraestructure.authorization;

import net.pladema.permissions.repository.enums.ERole;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementa hasPermission que se puede acceder desde las anotaciones
 * de seguridad del estilo @PreAuthorize("hasPermission(....)").
 */
@Component
public class InstitutionPermissionEvaluator implements PermissionEvaluator {
    
	@Override
	/**
	 * targetDomainObject: id de la institución
	 * permission: tiene que ser uno de los definidos en ERole
	 */
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
		if (permission instanceof String) {
			List<String> permissions = new ArrayList<>(Arrays.asList(((String)permission).split(",")));
			return hasPermission(auth, targetDomainObject, permissions);
		}
    	return false;
    }

    private boolean hasPermission(Authentication auth, Object targetDomainObject, List<String> permission) {
    	return permission.stream().anyMatch(p -> hasRoleInInstitution(auth, (Integer) targetDomainObject,
				ERole.valueOf(StringUtils.deleteWhitespace(p))));
	}

    @Override
    /**
     * targetId: id de la institución
     * targetType: por ahora tiene que ser "Institution"
     * permission: tiene que ser uno de los definidos en ERole 
     */
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
    	if (!targetType.equals("Institution")) return false;
    	return hasRoleInInstitution(auth, (Integer) targetId, ERole.valueOf((String) permission));
    }

	private boolean hasRoleInInstitution(Authentication auth, Integer targetId, ERole role) {
		List<InstitutionGrantedAuthority> authorities =
				(List<InstitutionGrantedAuthority>) auth.getAuthorities();
		return (authorities.stream().anyMatch(a -> a.getRoleAssignment().role.equals(role) && a.getRoleAssignment().institutionId.equals(targetId)));
	}
	
}
