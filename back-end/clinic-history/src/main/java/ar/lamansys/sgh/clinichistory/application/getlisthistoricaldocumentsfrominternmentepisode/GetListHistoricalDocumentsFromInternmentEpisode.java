package ar.lamansys.sgh.clinichistory.application.getlisthistoricaldocumentsfrominternmentepisode;

import ar.lamansys.sgh.clinichistory.domain.document.search.DocumentHistoricBo;
import ar.lamansys.sgh.clinichistory.domain.document.search.DocumentSearchBo;
import ar.lamansys.sgh.clinichistory.domain.document.search.DocumentSearchFilterBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.domain.DocumentCreatedOnSearchQuery;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.domain.DocumentDiagnosisSearchQuery;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.domain.DocumentDoctorSearchQuery;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.domain.DocumentSearchQuery;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.domain.DocumentSearchVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.domain.DocumentTypeSearchQuery;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetListHistoricalDocumentsFromInternmentEpisode {

    private static final String OUTPUT = "Output -> {}";

    private final DocumentRepository documentRepository;
    private final FeatureFlagsService featureFlagsService;

    public DocumentHistoricBo run(Integer internmentEpisodeId, DocumentSearchFilterBo searchFilter) {
        log.debug("Input parameters -> internmentEpisodeId {}, searchFilter {}", internmentEpisodeId, searchFilter);

        DocumentSearchQuery structuredQuery = defineDocumentSearchQuery(searchFilter);

        List<DocumentSearchVo> allDocuments = documentRepository.doHistoricSearch(internmentEpisodeId, structuredQuery);

        DocumentHistoricBo result = buildDocumentHistoricBo(allDocuments);

        log.debug(OUTPUT, result);
        return result;
    }

    @NonNull
    private DocumentSearchQuery defineDocumentSearchQuery(DocumentSearchFilterBo searchFilter) {
        DocumentSearchQuery structuredQuery;
        if (searchFilter != null) {
            String plainText = searchFilter.getPlainText();
            switch (searchFilter.getSearchType()) {
                case DOCTOR:
                    structuredQuery = new DocumentDoctorSearchQuery(plainText, featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));
                    break;
                case DIAGNOSIS:
                    structuredQuery = new DocumentDiagnosisSearchQuery(plainText);
                    break;
                case CREATED_ON:
                    structuredQuery = new DocumentCreatedOnSearchQuery(plainText);
                    break;
                case DOCUMENT_TYPE:
                    structuredQuery = new DocumentTypeSearchQuery(plainText);
                    break;
                default:
                    structuredQuery = new DocumentSearchQuery(plainText);
            }
        } else
            structuredQuery = new DocumentSearchQuery();
        return structuredQuery;
    }

    @NonNull
    private DocumentHistoricBo buildDocumentHistoricBo(List<DocumentSearchVo> allDocuments) {
        List<DocumentSearchBo> documentsBo = allDocuments.stream()
                .map(DocumentSearchBo::new)
                .peek(this::setEditedOn)
                .sorted(Comparator.comparing(DocumentSearchBo::getCreatedOn).reversed())
                .collect(Collectors.toList());

        return new DocumentHistoricBo(documentsBo);
    }

    private void setEditedOn(DocumentSearchBo documentSearchBo) {
        if (documentSearchBo.getInitialDocumentId() != null) {
            documentSearchBo.setEditedOn(documentSearchBo.getCreatedOn());
            documentRepository.findById(documentSearchBo.getInitialDocumentId())
                    .ifPresent(old -> documentSearchBo.setCreatedOn(old.getCreatedOn()));
        }
    }

}
