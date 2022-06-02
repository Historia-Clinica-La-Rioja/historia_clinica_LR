package net.pladema.programreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.programreports.repository.ConsultationDetailEpiI;
import net.pladema.programreports.repository.ConsultationDetailEpiII;
import net.pladema.programreports.repository.ConsultationDetailRecupero;

import java.util.List;

public interface ExcelServicePR {

	IWorkbook buildExcelEpidemiologiaI(String tittle, String[] headers, List<ConsultationDetailEpiI> query);

	IWorkbook buildExcelEpidemiologiaII(String tittle, String[] headers, List<ConsultationDetailEpiII> query);

	IWorkbook buildExcelRecupero(String tittle, String[] headers, List<ConsultationDetailRecupero> query);

}
