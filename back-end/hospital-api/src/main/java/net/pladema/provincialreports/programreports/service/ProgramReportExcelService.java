package net.pladema.provincialreports.programreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.programreports.repository.EpidemiologyOneConsultationDetail;
import net.pladema.provincialreports.programreports.repository.EpidemiologyTwoConsultationDetail;
import net.pladema.provincialreports.programreports.repository.RecuperoGeneralConsultationDetail;
import net.pladema.provincialreports.programreports.repository.RecuperoOdontologicoConsultationDetail;
import net.pladema.provincialreports.programreports.repository.SumarGeneralConsultationDetail;
import net.pladema.provincialreports.programreports.repository.OdontologicalConsultationDetail;

import java.util.List;

public interface ProgramReportExcelService {

	IWorkbook buildExcelEpidemiologyOne(String title, String[] headers, List<EpidemiologyOneConsultationDetail> query);

	IWorkbook buildExcelEpidemiologyTwo(String title, String[] headers, List<EpidemiologyTwoConsultationDetail> query);

	IWorkbook buildExcelRecuperoGeneral(String title, String[] headers, List<RecuperoGeneralConsultationDetail> query);

	IWorkbook buildExcelRecuperoOdontologico(String title, String[] headers, List<RecuperoOdontologicoConsultationDetail> query);

	IWorkbook buildExcelSumarGeneral(String title, String[] headers, List<SumarGeneralConsultationDetail> query);

	IWorkbook buildExcelOdontological(String title, String[] headers, List<OdontologicalConsultationDetail> query);

}
