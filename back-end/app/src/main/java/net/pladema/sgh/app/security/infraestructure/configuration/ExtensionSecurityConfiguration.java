package net.pladema.sgh.app.security.infraestructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.pladema.hsi.extensions.configuration.ExtensionAuthorization;

@Configuration
public class ExtensionSecurityConfiguration {

	@Bean
	public ExtensionAuthorization extensionAuthorization(
	)  {
		/**
		 * Por cada ítem de menú, define la función que valida que el usuario tenga
		 * los roles indicados
		 */
		return new ExtensionAuthorization() {

			@Override
			public boolean isSystemMenuAllowed(String menuId) {
				return false;
			}

			@Override
			public boolean isInstitutionMenuAllowed(String menuId, Integer institutionId) {
				return false;
			}
		};
	}

}
