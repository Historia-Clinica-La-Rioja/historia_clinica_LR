package net.pladema.generalreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.generalreports.repository.ComplementaryStudiesConsultationDetail;
import net.pladema.generalreports.repository.DiabeticHypertensionConsultationDetail;
import net.pladema.generalreports.repository.EmergencyConsultationDetail;

import java.util.List;

public interface GeneralReportExcelService {

	IWorkbook buildExcelEmergency(String title, String[] headers, List<EmergencyConsultationDetail> query);

	IWorkbook buildExcelDiabeticHypertension(String title, String[] headers, List<DiabeticHypertensionConsultationDetail> query);

	IWorkbook buildExcelComplementaryStudies(String title, String[] headers, List<ComplementaryStudiesConsultationDetail> query);

}
