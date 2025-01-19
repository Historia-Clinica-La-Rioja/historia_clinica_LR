package ar.lamansys.refcounterref.application.getrequestedreferences;

import ar.lamansys.refcounterref.application.port.ReferenceReportStorage;
import ar.lamansys.refcounterref.domain.report.ReferenceReportBo;
import ar.lamansys.refcounterref.domain.report.ReferenceReportFilterBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetRequestedReferences {

	private final ReferenceReportStorage referenceReportStorage;

	private final SharedLoggedUserPort sharedLoggedUserPort;

	private final SharedStaffPort sharedStaffPort;

	public Page<ReferenceReportBo> run(Integer institutionId, ReferenceReportFilterBo filter, Pageable pageable) {
		if (!userHasAdministrativeOrManagerRole(institutionId)) {
			var healthcareProfessionalId = sharedStaffPort.getProfessionalId(UserInfo.getCurrentAuditor());
			filter.setHealthcareProfessionalId(healthcareProfessionalId);
		}
		return referenceReportStorage.fetchReferencesReport(filter, pageable);
	}

	private boolean userHasAdministrativeOrManagerRole(Integer institutionId) {
		boolean hasAdministrativeRole = sharedLoggedUserPort.hasAdministrativeRole(institutionId);
		boolean hasInstitutionManagerRole = sharedLoggedUserPort.hasInstitutionalManagerRole();
		return hasAdministrativeRole || hasInstitutionManagerRole;
	}

}
