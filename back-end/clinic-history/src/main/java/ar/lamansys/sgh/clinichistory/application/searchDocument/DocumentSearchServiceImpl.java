package ar.lamansys.sgh.clinichistory.application.searchDocument;

import ar.lamansys.sgh.clinichistory.application.searchDocument.domain.DocumentSearchFilterBo;
import ar.lamansys.sgh.clinichistory.application.searchDocument.domain.EDocumentSearch;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.*;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentSearchServiceImpl implements DocumentSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentSearchServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentRepository documentRepository;

    public DocumentSearchServiceImpl(DocumentRepository documentRepository){
        this.documentRepository = documentRepository;
    }

    @Override
    public DocumentHistoricBo historicalListDocuments(Integer internmentEpisodeId, @Null DocumentSearchFilterBo searchFilter){
        LOG.debug("Input parameters -> internmentEpisodeId {}, searchFilter {}", internmentEpisodeId, searchFilter);
        DocumentSearchQuery structuredQuery;
        if(searchFilter != null) {
            String plainText = searchFilter.getPlainText();
            switch(searchFilter.getSearchType()){
                case DOCTOR:
                    structuredQuery = new DocumentDoctorSearchQuery(plainText);
                    break;
                case DIAGNOSIS:
                    structuredQuery = new DocumentDiagnosisSearchQuery(plainText);
                    break;
                case CREATED_ON:
                    structuredQuery = new DocumentCreatedOnSearchQuery(plainText);
                    break;
                default:
                    structuredQuery = new DocumentSearchQuery(plainText);
            }
        }
        else
            structuredQuery = new DocumentSearchQuery();
        List<DocumentSearchVo> allDocuments = documentRepository.historicSearch(internmentEpisodeId, structuredQuery);
        allDocuments = addProcedures(allDocuments);

        //No results Message
        String infoMessage = allDocuments.isEmpty() ? structuredQuery.noResultMessage() : Strings.EMPTY;
        List<DocumentSearchBo> documentsBo = allDocuments.stream().map(DocumentSearchBo::new).collect(Collectors.toList());

        DocumentHistoricBo result = new DocumentHistoricBo(documentsBo, infoMessage);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private List<DocumentSearchVo> addProcedures(List<DocumentSearchVo> documents){
        for (DocumentSearchVo d : documents){
            d.setProcedures(documentRepository.getProceduresByDocuments(d.getId()));
        }
        return documents;
    }
 }
