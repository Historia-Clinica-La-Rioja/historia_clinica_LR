package net.pladema.generalreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.generalreports.repository.ComplementaryStudies;
import net.pladema.generalreports.repository.ConsultationDetailDiabeticosHipertensos;
import net.pladema.generalreports.repository.ConsultationDetailEmergencias;
import net.pladema.generalreports.repository.HospitalizationOlderAdults;
import net.pladema.generalreports.repository.OutPatientOlderAdults;

import java.util.List;

public interface ExcelServiceGR {

	IWorkbook buildExcelEmergencias(String tittle, String[] headers, List<ConsultationDetailEmergencias> query);

	IWorkbook buildExcelDiabeticosHipertensos(String tittle, String[] headers, List<ConsultationDetailDiabeticosHipertensos> query);

	IWorkbook buildExcelComplementaryStudies(String tittle, String[] headers, List<ComplementaryStudies> query);
	
	IWorkbook buildExcelOutPatientOlderAdults(String tittle, String[] headers, List<OutPatientOlderAdults> query, String startDate, String endDate);

	IWorkbook buildExcelHospitalizationOlderAdults(String tittle, String[] headers, List<HospitalizationOlderAdults> query, String startDate, String endDate);

}