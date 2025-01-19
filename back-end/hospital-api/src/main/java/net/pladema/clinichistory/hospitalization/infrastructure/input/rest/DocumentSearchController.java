package net.pladema.clinichistory.hospitalization.infrastructure.input.rest;

import ar.lamansys.sgh.clinichistory.domain.document.search.DocumentHistoricBo;
import ar.lamansys.sgh.clinichistory.application.getlisthistoricaldocumentsfrominternmentepisode.GetListHistoricalDocumentsFromInternmentEpisode;
import ar.lamansys.sgh.clinichistory.domain.document.search.DocumentSearchFilterBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.searchDocument.dto.DocumentSearchFilterDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto.DocumentHistoricDto;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.mapper.DocumentSearchMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Document search", description = "Document search")
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/documentSearch")
@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
public class DocumentSearchController {

    public static final String OUTPUT = "Output -> {}";

    private final GetListHistoricalDocumentsFromInternmentEpisode getListHistoricalDocumentsFromInternmentEpisode;
    private final DocumentSearchMapper documentSearchMapper;
    private final ObjectMapper jackson;

    @GetMapping
    @InternmentValid
    public ResponseEntity<DocumentHistoricDto> getHCEList(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestParam(name = "searchFilter", required = false) String searchFilterStr) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, searchFilter {}",
                institutionId, internmentEpisodeId, searchFilterStr);

        DocumentSearchFilterBo filter = mapFilter(searchFilterStr);
        DocumentHistoricBo documentHistoric = getListHistoricalDocumentsFromInternmentEpisode.run(internmentEpisodeId, filter);
        DocumentHistoricDto result = documentSearchMapper.toDocumentHistoricDto(documentHistoric);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

    private DocumentSearchFilterBo mapFilter(String searchFilterStr) {
        try {
            if (searchFilterStr != null && !searchFilterStr.equals("undefined") && !searchFilterStr.equals("null")) {
                DocumentSearchFilterDto filterDto = jackson.readValue(searchFilterStr, DocumentSearchFilterDto.class);
                return new DocumentSearchFilterBo(filterDto.getPlainText(), filterDto.getSearchType());
            }
        } catch (IOException e) {
            log.error(String.format("Error mappeando filter: %s", searchFilterStr), e);
        }
        return null;
    }
}
