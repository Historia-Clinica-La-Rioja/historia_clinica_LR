package net.pladema.emergencycare.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.application.getemergencycaredocumentheader.GetEmergencyCareDocumentHeader;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareDocumentHeaderDto;
import net.pladema.emergencycare.infrastructure.input.rest.mapper.EmergencyCareDocumentHeaderMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Emergency care document header", description = "Emergency care document header")
@RequestMapping("/institutions/{institutionId}/emergency-care/{emergencyCareEpisodeId}/document-header")
@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
public class EmergencyCareDocumentHeaderController {

    private final GetEmergencyCareDocumentHeader getEmergencyCareDocumentHeader;
    private final EmergencyCareDocumentHeaderMapper headerMapper;

    @GetMapping("/{documentId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    @ResponseBody EmergencyCareDocumentHeaderDto get(
            @PathVariable Integer institutionId,
            @PathVariable Integer emergencyCareEpisodeId,
            @PathVariable Long documentId
    ) {
        log.trace("Input parameters -> institutionId {}, emergencyCareEpisodeId {}, documentId {}", institutionId, emergencyCareEpisodeId, documentId);
        var result = headerMapper.toEmergencyCareDocumentHeaderDto(
                getEmergencyCareDocumentHeader.run(emergencyCareEpisodeId, documentId)
        );
        log.trace("Output -> {}", result);
        return result;
    }

}
