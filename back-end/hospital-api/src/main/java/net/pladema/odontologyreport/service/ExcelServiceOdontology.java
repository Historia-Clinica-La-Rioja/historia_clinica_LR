package net.pladema.odontologyreport.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.odontologyreport.repository.ConsultationDetailOdontology;

import java.util.List;

public interface ExcelServiceOdontology {

	IWorkbook buildExcelOdontology(String tittle, String[] headers, List<ConsultationDetailOdontology> query);

}
