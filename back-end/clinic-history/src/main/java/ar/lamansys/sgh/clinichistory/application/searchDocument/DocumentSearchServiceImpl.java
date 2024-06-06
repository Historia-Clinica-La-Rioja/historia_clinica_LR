package ar.lamansys.sgh.clinichistory.application.searchDocument;

import ar.lamansys.sgh.clinichistory.application.searchDocument.domain.DocumentSearchFilterBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.DocumentCreatedOnSearchQuery;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.DocumentDiagnosisSearchQuery;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.DocumentDoctorSearchQuery;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.DocumentSearchQuery;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.DocumentSearchVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.DocumentTypeSearchQuery;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Null;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentSearchServiceImpl implements DocumentSearchService {

    private static final String OUTPUT = "Output -> {}";

    private final DocumentRepository documentRepository;
    private final FeatureFlagsService featureFlagsService;

    @Override
    public DocumentHistoricBo getListHistoricalDocuments(Integer internmentEpisodeId, @Null DocumentSearchFilterBo searchFilter) {
        log.debug("Input parameters -> internmentEpisodeId {}, searchFilter {}", internmentEpisodeId, searchFilter);

        DocumentSearchQuery structuredQuery = defineDocumentSearchQuery(searchFilter);

        List<DocumentSearchVo> allDocuments = documentRepository.doHistoricSearch(internmentEpisodeId, structuredQuery)
                .stream()
                .peek(this::setProcedures)
                .collect(Collectors.toList());

        DocumentHistoricBo result = buildDocumentHistoricBo(allDocuments, structuredQuery);

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

    private void setProcedures(DocumentSearchVo d) {
        d.setProcedures(documentRepository.getProceduresByDocuments(d.getId(), ProceduresStatus.ERROR));
    }

    @NonNull
    private DocumentHistoricBo buildDocumentHistoricBo(List<DocumentSearchVo> allDocuments, DocumentSearchQuery structuredQuery) {
        List<DocumentSearchBo> documentsBo = allDocuments.stream()
                .map(DocumentSearchBo::new)
                .peek(this::setEditedOn)
                .sorted(Comparator.comparing(DocumentSearchBo::getCreatedOn).reversed())
                .collect(Collectors.toList());

        String infoMessage = getInfoMessage(allDocuments, structuredQuery);
        return new DocumentHistoricBo(documentsBo, infoMessage);
    }

    private void setEditedOn(DocumentSearchBo documentSearchBo) {
        if (documentSearchBo.getInitialDocumentId() != null) {
            documentSearchBo.setEditedOn(documentSearchBo.getCreatedOn());
            documentRepository.findById(documentSearchBo.getInitialDocumentId())
                    .ifPresent(old -> documentSearchBo.setCreatedOn(old.getCreatedOn()));
        }
    }

    private static String getInfoMessage(List<DocumentSearchVo> allDocuments, DocumentSearchQuery structuredQuery) {
        return allDocuments.isEmpty() ? structuredQuery.noResultMessage() : Strings.EMPTY;
    }

}
