package net.pladema.internation.controller.documents.searchdocument;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.documents.searchdocument.dto.DocumentHistoricDto;
import net.pladema.internation.controller.documents.searchdocument.dto.DocumentSearchFilterDto;
import net.pladema.internation.controller.documents.searchdocument.mapper.DocumentSearchMapper;
import net.pladema.internation.service.documents.searchdocument.DocumentSearchService;
import net.pladema.internation.service.documents.searchdocument.domain.DocumentHistoricBo;
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
@Api(value = "documentSearch", tags = { "documentSearch" })
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
    //TODO: validar el formato de fecha con EDocumentSearch.CREATEDON
    public ResponseEntity<DocumentHistoricDto> historic(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestParam(name = "searchFilter", required = false) String searchFilterStr){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, searchFilter {}",
                institutionId, internmentEpisodeId, searchFilterStr);

        DocumentSearchFilterDto filter = null;
        try {
            if(searchFilterStr != null && !searchFilterStr.equals("undefined"))
                filter  = jackson.readValue(searchFilterStr, DocumentSearchFilterDto.class);
        }
        catch(IOException e){
            LOG.error(String.format("Error mappeando filter: %s", searchFilterStr), e);
        }
        DocumentHistoricBo documentHistoric = documentSearchService.historicalListDocuments(internmentEpisodeId, filter);
        DocumentHistoricDto result = documentSearchMapper.toDocumentHistoricDto(documentHistoric);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok(result);
    }
}
