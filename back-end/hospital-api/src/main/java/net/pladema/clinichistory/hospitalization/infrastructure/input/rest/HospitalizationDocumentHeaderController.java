package net.pladema.clinichistory.hospitalization.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.getdocumentheader.GetHospitalizationDocumentHeader;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto.HospitalizationDocumentHeaderDto;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.mapper.HospitalizationDocumentHeaderMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Hospitalization document header", description = "Hospitalization document header")
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/document-header")
@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
public class HospitalizationDocumentHeaderController {

    private final GetHospitalizationDocumentHeader getHospitalizationDocumentHeader;
    private final HospitalizationDocumentHeaderMapper headerMapper;

    @GetMapping("/{documentId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<HospitalizationDocumentHeaderDto> get(@PathVariable Integer institutionId,
                                                                @PathVariable Integer internmentEpisodeId,
                                                                @PathVariable Long documentId) {
        log.trace("Input parameters -> institutionId {}, internmentEpisodeId {}, documentId {}", institutionId, internmentEpisodeId, documentId);
        var resultBo = getHospitalizationDocumentHeader.run(institutionId, internmentEpisodeId, documentId);
        var result = headerMapper.toHospitalizationDocumentHeaderDto(resultBo);
        log.trace("Output -> {}", result);
        return ResponseEntity.ok(result);
    }
}
