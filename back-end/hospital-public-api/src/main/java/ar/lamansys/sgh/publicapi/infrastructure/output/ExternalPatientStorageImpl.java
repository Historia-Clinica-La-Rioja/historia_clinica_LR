package ar.lamansys.sgh.publicapi.infrastructure.output;

import ar.lamansys.sgh.publicapi.application.port.out.ExternalPatientStorage;
import ar.lamansys.sgh.publicapi.domain.ExternalPatientBo;
import ar.lamansys.sgh.publicapi.domain.ExternalPatientExtendedBo;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalPatientBoException;
import ar.lamansys.sgh.shared.infrastructure.input.service.RequiredPatientDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExternalPatientStorageImpl implements ExternalPatientStorage {

    private final ExternalPatientRepository externalPatientRepository;

    private final SharedPatientPort sharedPatientPort;

    @Override
    public Optional<ExternalPatientBo> findByExternalId(String externalId) {
        log.debug("Input parameters -> externalId {}", externalId);
        Optional<ExternalPatientBo> result = externalPatientRepository.findByExternalId(externalId)
                .map(externalPatient -> {
                    try {
                        return new ExternalPatientBo(
                                externalPatient.getId(),
                                externalPatient.getPatientId(),
                                externalPatient.getExternalId(),
                                externalPatient.getExternalEncounterId(),
                                externalPatient.getExternalEncounterDate(),
                                externalPatient.getExternalEncounterType());
                    } catch (ExternalPatientBoException e) {
                        return null;
                    }
                });
        log.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Integer save(ExternalPatientBo externalPatientBo) {
        log.debug("Input parameters -> externalPatientBo {}", externalPatientBo);
        ExternalPatient saved = externalPatientRepository.save(mapToEntity(externalPatientBo));
        Integer result = saved.getId();
        log.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<Integer> getPatientId(Short identificationTypeId, String identificationNumber, Short genderId) {
        log.debug("Input parameters -> identificationTypeId {}, identificationNumber {}, genderId {}",
                identificationTypeId, identificationNumber, genderId);
        List<Integer> idsPatient = sharedPatientPort.getPatientId(identificationTypeId, identificationNumber, genderId);
        Optional<Integer> result = idsPatient.stream().findFirst();
        log.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Integer createPatient(ExternalPatientExtendedBo epeBo) {
        log.debug("Input parameter externalPatientExtended -> {}", epeBo);
        Integer result = sharedPatientPort.createPatient(mapToRequiredPatientDataDto(epeBo));
        log.debug("Output -> {}", result);
        return result;
    }

    private RequiredPatientDataDto mapToRequiredPatientDataDto(ExternalPatientExtendedBo epeBo) {
        return new RequiredPatientDataDto(
                epeBo.getBirthDate(),
                epeBo.getFirstName(),
                epeBo.getGenderId(),
                epeBo.getIdentificationNumber(),
                epeBo.getIdentificationTypeId(),
                epeBo.getLastName(),
                epeBo.getPhoneNumber(),
                epeBo.getEmail());
    }

    private ExternalPatient mapToEntity(ExternalPatientBo epBo) {
        return new ExternalPatient(
                epBo.getPatientId(),
                epBo.getExternalId(),
                epBo.getExternalEncounterId(),
                epBo.getExternalEncounterDate(),
                epBo.getEExternalEncounterType().toString());
    }
}
