package net.pladema.programreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.programreports.repository.EpidemiologyOneConsultationDetail;
import net.pladema.programreports.repository.EpidemiologyTwoConsultationDetail;
import net.pladema.programreports.repository.RecuperoGeneralConsultationDetail;
import net.pladema.programreports.repository.RecuperoOdontologicoConsultationDetail;
import net.pladema.programreports.repository.SumarGeneralConsultationDetail;
import net.pladema.programreports.repository.SumarOdontologicoConsultationDetail;

import java.util.List;

public interface ProgramReportExcelService {

	IWorkbook buildExcelEpidemiologyOne(String title, String[] headers, List<EpidemiologyOneConsultationDetail> query);

	IWorkbook buildExcelEpidemiologyTwo(String title, String[] headers, List<EpidemiologyTwoConsultationDetail> query);

	IWorkbook buildExcelRecuperoGeneral(String title, String[] headers, List<RecuperoGeneralConsultationDetail> query);

	IWorkbook buildExcelRecuperoOdontologico(String title, String[] headers, List<RecuperoOdontologicoConsultationDetail> query);

	IWorkbook buildExcelSumarGeneral(String title, String[] headers, List<SumarGeneralConsultationDetail> query);

	IWorkbook buildExcelSumarOdontologico(String title, String[] headers, List<SumarOdontologicoConsultationDetail> query);

}
