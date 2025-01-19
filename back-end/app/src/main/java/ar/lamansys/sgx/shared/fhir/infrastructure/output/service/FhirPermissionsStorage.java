package ar.lamansys.sgx.shared.fhir.infrastructure.output.service;

import ar.lamansys.sgx.shared.fhir.application.port.FhirPermissionsPort;

import lombok.RequiredArgsConstructor;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FhirPermissionsStorage implements FhirPermissionsPort {
	private final UserSessionStorage userSessionStorage;

	@Override
	public boolean canPostMedicationRequest() {
		return checkPermission(ERole.API_RECETAS);
	}

	@Override
	public boolean canPostDiagnosticReport() {
		return checkPermission(ERole.FHIR_POST_DIAGNOSTIC_REPORT);
	}

	/**
	 * True when the client sends a federar valid token or if the user has the FHIR_ACCESS_ALL_RESOURCES role.
	 * See {@link net.pladema.sgh.app.security.infraestructure.filters.FHIRApiAuthenticationFilter}
	 * @return
	 */
	private boolean canAccessAllFhirResources() {
		return checkPermission(ERole.FHIR_ACCESS_ALL_RESOURCES);
	}

	@Override
	public boolean shouldEnableFhirResources() {
		var shouldEnable = canPostDiagnosticReport() || canPostMedicationRequest() || canAccessAllFhirResources();
		return shouldEnable;
	}

	@Override
	public boolean canFetchServiceRequest() {
		return canAccessAllFhirResources();
	}

	@Override
	public boolean canFetchMedicationRequest() {
		return canAccessAllFhirResources();
	}

	@Override
	public boolean canFetchDocumentReference() {
		return canAccessAllFhirResources();
	}

	@Override
	public boolean canFetchBundle() {
		return canAccessAllFhirResources();
	}

	private boolean checkPermission(ERole roleToCheck){
		try {
			return userSessionStorage
			.getRolesAssigned()
			.anyMatch(roleAssignment -> roleAssignment.isRole(roleToCheck));
		} catch (Exception e){
			return false;
		}
	}
}
