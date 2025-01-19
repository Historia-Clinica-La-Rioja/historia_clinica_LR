package net.pladema.reports.application.ports;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.reports.domain.ReportSearchFilterBo;

public interface NominalECEpisodeDetailStorage {

	IWorkbook buildNominalECEpisodeDetailExcelReport(String title, ReportSearchFilterBo filter);

}
