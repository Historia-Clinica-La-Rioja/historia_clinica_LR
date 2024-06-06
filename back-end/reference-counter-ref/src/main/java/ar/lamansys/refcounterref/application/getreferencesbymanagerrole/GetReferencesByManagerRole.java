package ar.lamansys.refcounterref.application.getreferencesbymanagerrole;

import ar.lamansys.refcounterref.application.port.ReferenceReportStorage;
import ar.lamansys.refcounterref.domain.report.ReferenceReportBo;
import ar.lamansys.refcounterref.domain.report.ReferenceReportFilterBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetReferencesByManagerRole {

	private final ReferenceReportStorage referenceReportStorage;

	private final SharedLoggedUserPort sharedLoggedUserPort;


	public Page<ReferenceReportBo> run(ReferenceReportFilterBo filter, Pageable pageable) {
		var managerUserId = UserInfo.getCurrentAuditor();
		if (sharedLoggedUserPort.hasLocalManagerRoleOrRegionalManagerRole(managerUserId))
			filter.setManagerUserId(managerUserId);
		if (sharedLoggedUserPort.hasDomainManagerRole(managerUserId))
			filter.setDomainManager(true);
		return referenceReportStorage.fetchReferencesReport(filter, pageable);
	}

}
