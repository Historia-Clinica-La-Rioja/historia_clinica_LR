package ar.lamansys.sgh.publicapi.infrastructure.output;

import ar.lamansys.sgh.publicapi.application.port.out.ExternalEncounterStorage;
import ar.lamansys.sgh.publicapi.application.port.out.exceptions.ExternalEncounterStorageException;
import ar.lamansys.sgh.publicapi.application.port.out.exceptions.ExternalEncounterStorageExceptionEnum;
import ar.lamansys.sgh.publicapi.domain.EExternalEncounterType;
import ar.lamansys.sgh.publicapi.domain.ExternalEncounterBo;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalEncounterBoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExternalEncounterStorageImpl implements ExternalEncounterStorage {

    private final ExternalEncounterRepository externalEncounterRepository;

    @Override
    public void save(ExternalEncounterBo externalEncounterBo) {
        log.debug("Input parameters -> externalEncounterBo {}", externalEncounterBo);
        externalEncounterRepository.save(mapToEntity(externalEncounterBo));
    }

    @Override
    public ExternalEncounterBo get(String externalEncounterId) throws ExternalEncounterBoException {
        log.debug("Input parameters -> externalEncounterId {}", externalEncounterId);
        ExternalEncounter result = externalEncounterRepository.getByExternalEncounterId(externalEncounterId)
                .orElseThrow(() -> new ExternalEncounterStorageException(ExternalEncounterStorageExceptionEnum.EXTERNAL_ENCOUNTER_ID_NOT_EXISTS, String.format("El id de encuentro externo %s no existe", externalEncounterId)));
        return mapToBo(result);
    }

    @Override
    public boolean existsExternalEncounter(String externalEncounterId) {
        log.debug("Input parameters -> externalEncounterId {}", externalEncounterId);
        return externalEncounterRepository.getByExternalEncounterId(externalEncounterId).isPresent();
    }

    @Override
    public void delete(String externalEncounterId) {
        log.debug("Input parameters -> externalEncounterId {}", externalEncounterId);
        externalEncounterRepository.getByExternalEncounterId(externalEncounterId)
                .ifPresentOrElse(externalEncounterRepository::delete,
                        () -> {
                            throw new ExternalEncounterStorageException(ExternalEncounterStorageExceptionEnum.EXTERNAL_ENCOUNTER_ID_NOT_EXISTS, String.format("El id de encuentro externo %s no existe", externalEncounterId));
                        });
    }
    private ExternalEncounter mapToEntity(ExternalEncounterBo externalEncounter) {
        return new ExternalEncounter(
                externalEncounter.getId(),
                externalEncounter.getExternalId(),
                externalEncounter.getExternalEncounterId(),
                externalEncounter.getExternalEncounterDate(),
                externalEncounter.getEExternalEncounterType().getValue(),
                externalEncounter.getInstitutionId());
    }

    private ExternalEncounterBo mapToBo(ExternalEncounter externalEncounter) throws ExternalEncounterBoException {
        return new ExternalEncounterBo(
                externalEncounter.getId(),
                externalEncounter.getExternalId(),
                externalEncounter.getExternalEncounterId(),
                externalEncounter.getExternalEncounterDate(),
                EExternalEncounterType.valueOf(externalEncounter.getExternalEncounterType()),
                externalEncounter.getInstitutionId());
    }
}
