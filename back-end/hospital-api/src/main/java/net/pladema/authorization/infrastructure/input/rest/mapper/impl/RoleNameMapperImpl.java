package net.pladema.authorization.infrastructure.input.rest.mapper.impl;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.pladema.authorization.infrastructure.input.rest.mapper.RoleNameMapper;
import net.pladema.permissions.repository.enums.ERole;

@Slf4j
@Service
public class RoleNameMapperImpl implements RoleNameMapper {

	@Override
	public String getRoleDescription(ERole eRole) {
		Locale locale = LocaleContextHolder.getLocale();
		String description = getDescription(eRole);
		log.debug("Role {} is {} in {}", eRole, description, locale);
		return description;
	}

	private static String getDescription(ERole eRole) {
		switch (eRole) {
			case ROOT: return "ROOT";
			case ADMINISTRADOR: return "Administrador";
			case ESPECIALISTA_MEDICO: return "Especialista Médico";
			case PROFESIONAL_DE_SALUD: return "Profesional de la salud";
			case ADMINISTRATIVO: return "Administrativo";
			case ENFERMERO_ADULTO_MAYOR: return "Enfermero adulto mayor";
			case ENFERMERO: return "Enfermero";
			case ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE:  return "Administrador institucional";
			case ADMINISTRADOR_AGENDA:  return "Administrador agenda";
			case API_CONSUMER:  return "Api consumer";
			case ESPECIALISTA_EN_ODONTOLOGIA:  return "Especialista en odontología";
			case ADMINISTRADOR_DE_CAMAS: return "Administrador de camas";
			case PERSONAL_DE_IMAGENES: return "Personal de Imágenes";
			case PERSONAL_DE_LABORATORIO: return "Personal de Laboratorio";
			case PERSONAL_DE_FARMACIA: return "Personal de Farmacia";
			case PERSONAL_DE_ESTADISTICA: return "Personal de estadística";
			case PARTIALLY_AUTHENTICATED: return "Usuario parcialmente autenticado";
		}
		throw new NotFoundException("role-not-exists", String.format("El rol %s no existe", eRole));
	}
}
