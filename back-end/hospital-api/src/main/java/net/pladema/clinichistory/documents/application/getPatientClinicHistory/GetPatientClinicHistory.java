package net.pladema.clinichistory.documents.application.getPatientClinicHistory;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.documents.application.ClinicHistoryStorage;
import net.pladema.clinichistory.documents.domain.CHDocumentSummaryBo;
import net.pladema.clinichistory.documents.domain.CHSearchFilterBo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetPatientClinicHistory {

    private final ClinicHistoryStorage clinicHistoryStorage;
    private final FeatureFlagsService featureFlagsService;

    private final List<Short> UNDEFINED_DOCUMENT_TYPES = Stream.of(DocumentType.MEDICAL_IMAGE_REPORT,
            DocumentType.SURGICAL_HOSPITALIZATION_REPORT,
            DocumentType.EMERGENCY_SURGICAL_REPORT)
            .collect(Collectors.toList());

	@PostConstruct
	public void init() {
		boolean anestheticReportIsNotDefined = !featureFlagsService.isOn(AppFeature.HABILITAR_PARTE_ANESTESICO_EN_DESARROLLO);
		if (anestheticReportIsNotDefined)
			UNDEFINED_DOCUMENT_TYPES.add(DocumentType.ANESTHETIC_REPORT);
	}

    public List<CHDocumentSummaryBo> run(Integer patientId, LocalDate from, LocalDate to, CHSearchFilterBo filter) {
        log.debug("Input parameters -> patientId {}, from {}, to {}, filter {}", patientId, from, to, filter);
        var result = clinicHistoryStorage.getPatientClinicHistory(patientId, from, to)
                .stream()
                .filter(o -> filterByEncounterType(filter, o)
                        && filterByDocumentType(filter, o)
                        && isDocumentDefined(o))
                .collect(Collectors.toList());
        log.debug("Output -> {}", result);
        return result;
    }

    private boolean isDocumentDefined(CHDocumentSummaryBo o) {
        return !UNDEFINED_DOCUMENT_TYPES.contains(o.getTypeId());
    }

    private boolean filterByDocumentType(CHSearchFilterBo filter, CHDocumentSummaryBo o) {
        return filter.getDocumentTypeList().isEmpty() || filter.getDocumentTypeList().contains(o.getDocumentType());
    }

    private boolean filterByEncounterType(CHSearchFilterBo filter, CHDocumentSummaryBo o) {
        return filter.getEncounterTypeList().contains(o.getEncounterType());
    }

}
