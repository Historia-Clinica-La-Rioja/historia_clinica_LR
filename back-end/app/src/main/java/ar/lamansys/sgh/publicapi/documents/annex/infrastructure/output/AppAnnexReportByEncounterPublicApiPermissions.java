package ar.lamansys.sgh.publicapi.documents.annex.infrastructure.output;

import java.util.Optional;

import ar.lamansys.sgh.publicapi.ApiConsumerCondition;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.documents.annex.application.AnnexReportByEncounterPublicApiPermissionsPort;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;

@RequiredArgsConstructor
@Service
public class AppAnnexReportByEncounterPublicApiPermissions implements AnnexReportByEncounterPublicApiPermissionsPort {
	private final UserSessionStorage userSessionStorage;
	private final InstitutionRepository institutionRepository;
	private final ApiConsumerCondition apiConsumerCondition;


	@Override
	public boolean canAccess(Integer institutionId) {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssignment ->
					roleAssignment.isAssigment(ERole.API_ANEXO, institutionId)
					|| apiConsumerCondition.isRole(roleAssignment)
		);
	}

	@Override
	public Optional<Integer> findInstitutionId(String refsetCode) {
		return institutionRepository.findIdBySisaCode(refsetCode);
	}

}
