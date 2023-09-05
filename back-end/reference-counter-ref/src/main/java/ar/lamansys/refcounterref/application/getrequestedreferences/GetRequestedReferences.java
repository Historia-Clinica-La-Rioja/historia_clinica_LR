package ar.lamansys.refcounterref.application.getrequestedreferences;

import ar.lamansys.refcounterref.application.port.ReferenceReportStorage;
import ar.lamansys.refcounterref.domain.ReferenceReportBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GetRequestedReferences {

	private final ReferenceReportStorage referenceReportStorage;

	private final SharedLoggedUserPort sharedLoggedUserPort;

	private final SharedStaffPort sharedStaffPort;


	public List<ReferenceReportBo> run(Integer institutionId, LocalDate from, LocalDate to) {
		boolean hasAdministrativeRole = sharedLoggedUserPort.hasAdministrativeRole(institutionId);
		if (hasAdministrativeRole)
			return referenceReportStorage.fetchRequestedReferencesReport(institutionId, null, from, to);
		var healthcareProfessionalId = sharedStaffPort.getProfessionalId(UserInfo.getCurrentAuditor());
		return referenceReportStorage.fetchRequestedReferencesReport(institutionId, healthcareProfessionalId, from, to);
	}
}
