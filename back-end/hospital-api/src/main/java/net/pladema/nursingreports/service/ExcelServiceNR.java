package net.pladema.nursingreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.nursingreports.repository.HospitalizationNursing;
import net.pladema.nursingreports.repository.OutpatientNursing;
import net.pladema.nursingreports.repository.NursingEmergencies;
import net.pladema.nursingreports.repository.TotalNursingRecovery;
import net.pladema.nursingreports.repository.VaccinesNursing;

import java.util.List;

public interface ExcelServiceNR {

	IWorkbook buildExcelHospitalizationNursing(String tittle, String[] headers, List<HospitalizationNursing> query);

	IWorkbook buildExcelOutpatientNursing(String tittle, String[] headers, List<OutpatientNursing> query);

	IWorkbook buildExcelNursingEmergencies(String tittle, String[] headers, List<NursingEmergencies> query);

	IWorkbook buildExcelTotalNursingRecovery(String tittle, String[] headers, List<TotalNursingRecovery> query);

	IWorkbook buildExcelVaccinesNursing(String title, String[] headers, List<VaccinesNursing> query);

}