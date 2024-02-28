package net.pladema.reports.imageNetworkProductivity.application.port;

import net.pladema.reports.imageNetworkProductivity.domain.InstitutionBo;

public interface ImageNetworkProductivityReportInstitutionStorage {

	InstitutionBo fetchInstitutionData(Integer institutionId);

}
