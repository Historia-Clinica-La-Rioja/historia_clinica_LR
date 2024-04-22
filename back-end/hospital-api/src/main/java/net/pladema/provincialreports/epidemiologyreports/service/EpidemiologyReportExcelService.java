package net.pladema.provincialreports.epidemiologyreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.epidemiologyreports.repository.DenguePatientControlConsultationDetail;

import java.util.List;

public interface EpidemiologyReportExcelService {

	IWorkbook buildExcelDenguePatientControl(String title, String[] headers, List<DenguePatientControlConsultationDetail> query, String institutionName, String observations);

}
