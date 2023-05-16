package net.pladema.clinichistory.documents.application.getPatientClinicHistoryLastDownload;

import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.documents.application.ClinicHistoryStorage;

import net.pladema.clinichistory.documents.domain.HistoricClinicHistoryDownloadBo;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GetPatientClinicHistoryLastDownload {

	private final ClinicHistoryStorage clinicHistoryStorage;

	public GetPatientClinicHistoryLastDownload (ClinicHistoryStorage clinicHistoryStorage){
		this.clinicHistoryStorage = clinicHistoryStorage;
	}

	public HistoricClinicHistoryDownloadBo run (Integer patientId, Integer institutionId){
		log.debug("Input parameters -> patientId {}, institutionId {}", patientId, institutionId);
		var result = clinicHistoryStorage.getPatientClinicHistoryLastDownload(patientId, institutionId);
		log.debug("Output -> {}", result);
		return result.orElse(new HistoricClinicHistoryDownloadBo(null, patientId, institutionId, null, null));
	}

}
