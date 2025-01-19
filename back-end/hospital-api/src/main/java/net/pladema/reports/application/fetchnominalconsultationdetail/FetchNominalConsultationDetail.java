package net.pladema.reports.application.fetchnominalconsultationdetail;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.reports.application.ports.NominalConsultationDetailStorage;
import net.pladema.reports.repository.ConsultationDetail;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class FetchNominalConsultationDetail {

	private final NominalConsultationDetailStorage nominalConsultationDetailStorage;

	public IWorkbook run(String title, String[] headers, List<ConsultationDetail> consultationDetails, Integer institutionId){
		log.debug("Build Nominal Consultation Detail Report - Hoja 2");
		return nominalConsultationDetailStorage.buildNominalExternalConsultationDetailReport(title, headers, consultationDetails, institutionId);
	}
}
