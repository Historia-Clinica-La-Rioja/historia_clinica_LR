package net.pladema.reports.application.ports;

import net.pladema.reports.repository.InstitutionInfo;

public interface InstitutionReportStorage {

	InstitutionInfo getInstitutionInfo(Integer institutionId);

}
