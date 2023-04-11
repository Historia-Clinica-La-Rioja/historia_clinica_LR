package ar.lamansys.sgh.clinichistory.application.searchDocument;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.searchDocument.domain.DocumentSearchFilterBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.*;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentSearchServiceImpl implements DocumentSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentSearchServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentRepository documentRepository;

	private final FeatureFlagsService featureFlagsService;

	private final DocumentService documentService;

    public DocumentSearchServiceImpl(DocumentRepository documentRepository, FeatureFlagsService featureFlagsService, DocumentService documentService){
        this.documentRepository = documentRepository;
		this.featureFlagsService = featureFlagsService;
		this.documentService = documentService;
	}

    @Override
    public DocumentHistoricBo historicalListDocuments(Integer internmentEpisodeId, @Null DocumentSearchFilterBo searchFilter){
        LOG.debug("Input parameters -> internmentEpisodeId {}, searchFilter {}", internmentEpisodeId, searchFilter);
        DocumentSearchQuery structuredQuery;
        if(searchFilter != null) {
            String plainText = searchFilter.getPlainText();
            switch(searchFilter.getSearchType()){
                case DOCTOR:
                    structuredQuery = new DocumentDoctorSearchQuery(plainText, featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));
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
		setEditedOn(documentsBo);
		documentsBo.sort(Comparator.comparing(DocumentSearchBo::getCreatedOn).reversed());
		DocumentHistoricBo result = new DocumentHistoricBo(documentsBo, infoMessage);
        LOG.debug(OUTPUT, result);
        return result;
    }

	@Override
	public List<EmergencyCareEpisodeTriageSearchBo> getEmergencyCareTriageHistoricalDocumentList(Integer emergencyCareEpisodeId) {
		LOG.debug("Input parameters -> emergencyCareEpisodeId {}", emergencyCareEpisodeId);
		List<EmergencyCareEpisodeTriageSearchBo> triages = documentRepository.getTriageDocumentData(emergencyCareEpisodeId).stream()
				.map(EmergencyCareEpisodeTriageSearchBo::new).collect(Collectors.toList());
		triages.forEach(triage -> {
			triage.setRiskFactors(documentService.getRiskFactorStateFromDocument(triage.getDocumentId()));
			triage.setOtherRiskFactors(documentService.getOtherRiskFactors(triage.getDocumentId()));
		});
		LOG.debug(OUTPUT, triages);
		return triages;
	}

    private List<DocumentSearchVo> addProcedures(List<DocumentSearchVo> documents){
        for (DocumentSearchVo d : documents){
            d.setProcedures(documentRepository.getProceduresByDocuments(d.getId(), ProceduresStatus.ERROR));
        }
        return documents;
    }

	private void setEditedOn(List<DocumentSearchBo> documentsBo) {
		documentsBo.forEach(d -> {
			if (d.getInitialDocumentId() != null) {
				d.setEditedOn(d.getCreatedOn());
				documentRepository.findById(d.getInitialDocumentId()).ifPresent(old -> d.setCreatedOn(old.getCreatedOn()));
			}
		});
	}


 }
