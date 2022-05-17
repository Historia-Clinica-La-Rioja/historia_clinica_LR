package ar.lamansys.sgh.publicapi.infrastructure.input.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.application.deleteexternalencounter.DeleteExternalEncounter;
import ar.lamansys.sgh.publicapi.application.saveexternalencounter.SaveExternalEncounter;
import ar.lamansys.sgh.publicapi.domain.ExternalEncounterBo;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalEncounterBoException;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.ExternalEncounterDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/public-api/patient/{externalId}/institution/{institutionId}/external-encounters")
@Tag(name = "Public Api", description = "External Encounter")
public class ExternalEncounterController {

    private final SaveExternalEncounter saveExternalEncounter;

    private final DeleteExternalEncounter deleteExternalEncounter;

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public void save(@PathVariable("externalId") String externalId,
                     @PathVariable("institutionId") Integer institutionId,
                     @RequestBody ExternalEncounterDto externalEncounterDto) throws ExternalEncounterBoException {
        log.debug("Input parameters -> externalId {}, institutionId {}, externalEncounterDto {}",externalId, institutionId, externalEncounterDto);
        saveExternalEncounter.run(toExternalEncounterBo(externalEncounterDto,externalId, institutionId));
    }

    @DeleteMapping("/{externalEncounterId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void delete(@PathVariable("externalId") String externalId,
                       @PathVariable("institutionId") Integer institutionId,
                       @PathVariable("externalEncounterId") String externalEncounterId) throws ExternalEncounterBoException {
        log.debug("Input parameters -> externalId {}, institutionId {}, externalEncounterId {}",externalId, institutionId, externalEncounterId);
        deleteExternalEncounter.run(externalEncounterId, institutionId);
    }


    private ExternalEncounterBo toExternalEncounterBo(ExternalEncounterDto externalEncounter, String externalId, Integer institutionId) throws ExternalEncounterBoException {
        return new ExternalEncounterBo(
                externalEncounter.getId(),
                externalId,
                externalEncounter.getExternalEncounterId(),
                externalEncounter.getExternalEncounterDate(),
                externalEncounter.getExternalEncounterType(),
                institutionId);
    }
}
