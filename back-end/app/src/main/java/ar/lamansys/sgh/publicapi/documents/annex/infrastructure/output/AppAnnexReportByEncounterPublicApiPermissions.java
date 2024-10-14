package ar.lamansys.sgh.publicapi.documents.annex.infrastructure.output;

import ar.lamansys.sgh.publicapi.ApiConsumerCondition;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.documents.annex.application.AnnexReportByEncounterPublicApiPermissionsPort;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;

@RequiredArgsConstructor
@Service
public class AppAnnexReportByEncounterPublicApiPermissions implements AnnexReportByEncounterPublicApiPermissionsPort {
	private final UserSessionStorage userSessionStorage;
	private final ApiConsumerCondition apiConsumerCondition;

	@Override
	public boolean canAccess() {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssignment ->
					roleAssignment.isAssigment(ERole.API_ANEXO, -1)
					|| apiConsumerCondition.isRole(roleAssignment)
		);
	}

}

