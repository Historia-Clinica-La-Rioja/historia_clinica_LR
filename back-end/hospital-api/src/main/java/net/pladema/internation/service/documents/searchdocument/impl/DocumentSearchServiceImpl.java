package net.pladema.internation.service.documents.searchdocument.impl;

import net.pladema.internation.controller.documents.searchdocument.dto.DocumentSearchFilterDto;
import net.pladema.internation.repository.documents.DocumentRepository;
import net.pladema.internation.repository.documents.searchdocument.DocumentCreatedOnSearchQuery;
import net.pladema.internation.repository.documents.searchdocument.DocumentDiagnosisSearchQuery;
import net.pladema.internation.repository.documents.searchdocument.DocumentDoctorSearchQuery;
import net.pladema.internation.repository.documents.searchdocument.DocumentSearchQuery;
import net.pladema.internation.repository.documents.searchdocument.DocumentSearchVo;
import net.pladema.internation.service.documents.searchdocument.DocumentSearchService;
import net.pladema.internation.service.documents.searchdocument.domain.DocumentHistoricBo;
import net.pladema.internation.service.documents.searchdocument.domain.DocumentSearchBo;
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
    public DocumentHistoricBo historicalListDocuments(Integer internmentEpisodeId, @Null DocumentSearchFilterDto searchFilter){
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
                case CREATEDON:
                    structuredQuery = new DocumentCreatedOnSearchQuery(plainText);
                    break;
                default:
                    structuredQuery = new DocumentSearchQuery(plainText);
            }
        }
        else
            structuredQuery = new DocumentSearchQuery();
        List<DocumentSearchVo> allDocuments = documentRepository.historicSearch(internmentEpisodeId, structuredQuery);

        //No results Message
        String infoMessage = allDocuments.isEmpty() ? structuredQuery.noResultMessage() : Strings.EMPTY;
        List<DocumentSearchBo> documentsBo = allDocuments.stream().map(DocumentSearchBo::new).collect(Collectors.toList());

        DocumentHistoricBo result = new DocumentHistoricBo(documentsBo, infoMessage);
        LOG.debug(OUTPUT, result);
        return result;
    }
 }
