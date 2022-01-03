package ar.lamansys.sgh.publicapi.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.application.deleteexternalencounter.DeleteExternalEncounter;
import ar.lamansys.sgh.publicapi.application.saveexternalencounter.SaveExternalEncounter;
import ar.lamansys.sgh.publicapi.domain.ExternalEncounterBo;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalEncounterBoException;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.ExternalEncounterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/public-api/patient/{externalId}/external-encounters")
//@Api(value = "External Encounter", tags = {"External Encounter"})
public class ExternalEncounterController {

    private final SaveExternalEncounter saveExternalEncounter;

    private final DeleteExternalEncounter deleteExternalEncounter;

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public void save(@PathVariable("externalId") String externalId,
                     @RequestBody ExternalEncounterDto externalEncounterDto) throws ExternalEncounterBoException {
        log.debug("Input parameters -> externalId {}, externalEncounterDto {}",externalId, externalEncounterDto);
        saveExternalEncounter.run(toExternalEncounterBo(externalEncounterDto,externalId));
    }

    @DeleteMapping("/{externalEncounterId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void delete(@PathVariable("externalId") String externalId,
                       @PathVariable("externalEncounterId") String externalEncounterId){
        log.debug("Input parameters -> externalId {}, externalEncounterId {}",externalId, externalEncounterId);
        deleteExternalEncounter.run(externalEncounterId);
    }


    private ExternalEncounterBo toExternalEncounterBo(ExternalEncounterDto externalEncounter, String externalId) throws ExternalEncounterBoException {
        return new ExternalEncounterBo(
                externalEncounter.getId(),
                externalId,
                externalEncounter.getExternalEncounterId(),
                externalEncounter.getExternalEncounterDate(),
                externalEncounter.getExternalEncounterType());
    }
}
