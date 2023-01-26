package net.pladema.generalreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.generalreports.repository.ComplementaryStudies;
import net.pladema.generalreports.repository.ConsultationDetailDiabeticosHipertensos;
import net.pladema.generalreports.repository.ConsultationDetailEmergencias;
import net.pladema.generalreports.repository.NursingInternment;
import net.pladema.generalreports.repository.OutpatientNursing;
import net.pladema.generalreports.repository.PatientEmergencies;

import java.util.List;

public interface ExcelServiceGR {

	IWorkbook buildExcelEmergencias(String tittle, String[] headers, List<ConsultationDetailEmergencias> query);

	IWorkbook buildExcelDiabeticosHipertensos(String tittle, String[] headers, List<ConsultationDetailDiabeticosHipertensos> query);

	IWorkbook buildExcelPatientEmergencies(String tittle, String[] headers, List<PatientEmergencies> query);

	IWorkbook buildExcelOutpatientNursing(String tittle, String[] headers, List<OutpatientNursing> query);

	IWorkbook buildExcelNursingInternment(String tittle, String[] headers, List<NursingInternment> query);

	IWorkbook buildExcelComplementaryStudies(String tittle, String[] headers, List<ComplementaryStudies> query);

}