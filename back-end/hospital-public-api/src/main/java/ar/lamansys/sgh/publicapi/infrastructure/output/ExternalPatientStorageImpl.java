package ar.lamansys.sgh.publicapi.infrastructure.output;

import ar.lamansys.sgh.publicapi.application.port.out.ExternalPatientStorage;
import ar.lamansys.sgh.publicapi.domain.ExternalPatientBo;
import ar.lamansys.sgh.publicapi.domain.ExternalPatientCoverageBo;
import ar.lamansys.sgh.publicapi.domain.ExternalPatientExtendedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalCoverageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.RequiredPatientDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                .map(externalPatient -> new ExternalPatientBo(
                        externalPatient.getExternalPatientPK().getPatientId(),
                        externalPatient.getExternalPatientPK().getExternalId()));
        log.debug("Output -> {}", result);
        return result;
    }

    @Override
    public String save(ExternalPatientBo externalPatientBo) {
        log.debug("Input parameters -> externalPatientBo {}", externalPatientBo);
        ExternalPatient saved = externalPatientRepository.save(mapToEntity(externalPatientBo));
        String result = saved.getExternalPatientPK().getExternalId();
        log.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<Integer> getPatientId(ExternalPatientExtendedBo epeBo) {
        log.debug("Input parameters -> ExternalPatientExtendedBo {}", epeBo);
        List<Integer> idsPatient = sharedPatientPort.getPatientId(epeBo.getIdentificationTypeId(), epeBo.getIdentificationNumber(), epeBo.getGenderId());
        Optional<Integer> result = idsPatient.stream().findFirst();
        result.ifPresent(patientId -> {
            if(epeBo.getExternalId()!=null&&externalPatientRepository.findByPatientId(patientId).isEmpty())
                externalPatientRepository.save(new ExternalPatient(new ExternalPatientPK(epeBo.getExternalId(), patientId)));});
        log.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Integer createPatient(ExternalPatientExtendedBo epeBo) {
        log.debug("Input parameter externalPatientExtended -> {}", epeBo);
        Integer result = sharedPatientPort.createPatient(mapToRequiredPatientDataDto(epeBo));
        if(epeBo.getExternalId()!=null)
            externalPatientRepository.save(new ExternalPatient(new ExternalPatientPK(epeBo.getExternalId(), result)));
        log.debug("Output -> {}", result);
        return result;
    }

    @Override
    public void saveMedicalCoverages(ExternalPatientExtendedBo epeBo) {
        log.debug("Input parameter externalPatientExtended -> {}", epeBo);
        sharedPatientPort.saveMedicalCoverages(mapToExternalPatientCoverageListDto(epeBo.getMedicalCoverages()), epeBo.getPatientId());
    }

    private List<ExternalPatientCoverageDto> mapToExternalPatientCoverageListDto(List<ExternalPatientCoverageBo> medicalCoverageListBo) {
        List<ExternalPatientCoverageDto> result = new ArrayList<>();
        medicalCoverageListBo
                .forEach(mc -> result.add(new ExternalPatientCoverageDto(
                        new ExternalCoverageDto(
                                mc.getMedicalCoverage().getId(),
                                mc.getMedicalCoverage().getCuit(),
                                mc.getMedicalCoverage().getPlan(),
                                mc.getMedicalCoverage().getName(),
                                mc.getMedicalCoverage().getType().getId()),
                        mc.getAffiliateNumber(),
                        mc.getActive(),
                        mc.getVigencyDate(),
						mc.getCondition())));
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
                epeBo.getEmail(),
                epeBo.getInstitutionId());
    }

    private ExternalPatient mapToEntity(ExternalPatientBo epBo) {
        return new ExternalPatient(
                new ExternalPatientPK(epBo.getExternalId(),epBo.getPatientId()));
    }
}
