package net.pladema.sgh.app.security.infraestructure.configuration;

import static net.pladema.hsi.extensions.configuration.ExtensionAuthorizationBuilder.buildExtensionAuthorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.pladema.hsi.extensions.configuration.ExtensionAuthorization;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.LoggedUserService;

@Configuration
public class ExtensionSecurityConfiguration {

	@Bean
	public ExtensionAuthorization extensionAuthorization(
			LoggedUserService loggedUser
	)  {
		return buildExtensionAuthorization()
				.systemMenu("tableros", loggedUser.hasAnySystemRole(ERole.ADMINISTRADOR, ERole.ADMINISTRATIVO))
				.isInstitutionMenuAllowed("informacion", loggedUser.hasAnyInstitutionRole(ERole.PROFESIONAL_DE_SALUD))
				.build();
	}

}
