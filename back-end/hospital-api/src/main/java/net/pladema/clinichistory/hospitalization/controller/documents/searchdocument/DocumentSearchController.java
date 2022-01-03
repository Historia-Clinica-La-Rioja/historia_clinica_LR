package net.pladema.clinichistory.hospitalization.controller.documents.searchdocument;

import ar.lamansys.sgh.clinichistory.application.searchDocument.domain.DocumentSearchFilterBo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.dto.DocumentHistoricDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.searchDocument.dto.DocumentSearchFilterDto;
import net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.mapper.DocumentSearchMapper;
import ar.lamansys.sgh.clinichistory.application.searchDocument.DocumentSearchService;
import ar.lamansys.sgh.clinichistory.application.searchDocument.DocumentHistoricBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/documentSearch")
@Tag(name = "Document search", description = "Document search")
@Validated
public class DocumentSearchController {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentSearchController.class);
    public static final String OUTPUT = "Output -> {}";

    private final DocumentSearchService documentSearchService;

    private final DocumentSearchMapper documentSearchMapper;

    private final ObjectMapper jackson;

    public DocumentSearchController(DocumentSearchService documentSearchService,
                                    DocumentSearchMapper documentSearchMapper,
                                    ObjectMapper jackson){
        this.documentSearchService = documentSearchService;
        this.documentSearchMapper = documentSearchMapper;
        this.jackson = jackson;
    }

    @GetMapping
    @InternmentValid
    public ResponseEntity<DocumentHistoricDto> historic(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestParam(name = "searchFilter", required = false) String searchFilterStr){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, searchFilter {}",
                institutionId, internmentEpisodeId, searchFilterStr);

        DocumentSearchFilterBo filter = mapFilter(searchFilterStr);
        DocumentHistoricBo documentHistoric = documentSearchService.
                historicalListDocuments(internmentEpisodeId, filter);
        DocumentHistoricDto result = documentSearchMapper.toDocumentHistoricDto(documentHistoric);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

    private DocumentSearchFilterBo mapFilter(String searchFilterStr) {
        try {
            if(searchFilterStr != null && !searchFilterStr.equals("undefined") && !searchFilterStr.equals("null")) {
                DocumentSearchFilterDto filterDto = jackson.readValue(searchFilterStr, DocumentSearchFilterDto.class);
                return new DocumentSearchFilterBo(filterDto.getPlainText(), filterDto.getSearchType());
            }
        }
        catch(IOException e){
            LOG.error(String.format("Error mappeando filter: %s", searchFilterStr), e);
        }
        return null;
    }
}
