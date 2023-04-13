package net.pladema.clinichistory.documents.application.getPatientClinicHistory;

import net.pladema.clinichistory.documents.application.ClinicHistoryStorage;
import net.pladema.clinichistory.documents.domain.CHDocumentSummaryBo;
import net.pladema.clinichistory.documents.domain.CHSearchFilterBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GetPatientClinicHistory {

	private final ClinicHistoryStorage clinicHistoryStorage;

	public GetPatientClinicHistory(ClinicHistoryStorage clinicHistoryStorage){
		this.clinicHistoryStorage = clinicHistoryStorage;
	}

	public List<CHDocumentSummaryBo> run (Integer patientId, LocalDate from, LocalDate to, CHSearchFilterBo filter){
		log.debug("Input parameters -> patientId {}, from{}, to{}, filter{}", patientId, from, to, filter);
		var result = clinicHistoryStorage.getPatientClinicHistory(patientId, from, to)
				.stream()
				.filter(o -> filter.getEncounterTypeList().contains(o.getEncounterType())
						&& (filter.getDocumentTypeList().isEmpty() || filter.getDocumentTypeList().contains(o.getDocumentType())))
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

}
