package ar.lamansys.sgh.publicapi.activities.infrastructure.input.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.ApiConsumerCondition;
import lombok.AllArgsConstructor;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;

@AllArgsConstructor
@Service
public class AppActivitiesPublicApiPermissions implements ActivitiesPublicApiPermissions {

	private final UserSessionStorage userSessionStorage;
	private final ApiConsumerCondition apiConsumerCondition;

	private final InstitutionRepository institutionRepository;

	@Override
	public boolean canAccess(Integer institutionId) {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_FACTURACION, institutionId)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canFetchHealthcareProfessionals(Integer institutionId) {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_FACTURACION, institutionId)
		);
	}

	@Override
	public boolean canFetchAllMedicalCoverages() {
		//como son las coberturas médicas del sistema por ahora no se pide institucion especifica
		//ver HSI-10153
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isRole(ERole.API_FACTURACION)
		);
	}


	@Override
	public boolean canFetchAllInstitutions() {
		//como son las instituciones del sistema por ahora no se pide institucion especifica
		//ver HSI-10843
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isRole(ERole.API_FACTURACION)
		);
	}

	@Override
	public boolean canFetchHolidays() {
		//como son los feriados del sistema por ahora no se pide institucion especifica
		//ver HSI-10585
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isRole(ERole.API_FACTURACION)
		);
	}

	@Override
	public Optional<Integer> findInstitutionId(String refsetCode) {
		return institutionRepository.findIdBySisaCode(refsetCode);
	}

	@Override
	public boolean canAccessActivityInfo(Integer institutionId){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_FACTURACION, institutionId)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessProcessActivityInfo(Integer institutionId){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_FACTURACION, institutionId)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessActivityByFilter(Integer institutionId){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_FACTURACION, institutionId)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}


}
