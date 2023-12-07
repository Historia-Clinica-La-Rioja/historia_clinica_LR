package net.pladema.provincialreports.generalreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.generalreports.repository.ComplementaryStudiesConsultationDetail;
import net.pladema.provincialreports.generalreports.repository.DiabeticHypertensionConsultationDetail;
import net.pladema.provincialreports.generalreports.repository.EmergencyConsultationDetail;

import java.util.List;

public interface GeneralReportExcelService {

	IWorkbook buildExcelEmergency(String title, String[] headers, List<EmergencyConsultationDetail> query);

	IWorkbook buildExcelDiabeticHypertension(String title, String[] headers, List<DiabeticHypertensionConsultationDetail> query);

	IWorkbook buildExcelComplementaryStudies(String title, String[] headers, List<ComplementaryStudiesConsultationDetail> query);

}
