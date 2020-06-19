package net.pladema.clinichistory.hospitalization.service.patientDischarge;

import net.pladema.clinichistory.hospitalization.controller.mapper.PatientDischargeMapper;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientDischargeServiceImpl implements PatientDischargeService {

    private static final Logger LOG = LoggerFactory.getLogger(PatientDischargeServiceImpl.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";

    private final PatientDischargeRepository patientDischargeRepository;

    private final PatientDischargeMapper patientDischargeMapper;

    public PatientDischargeServiceImpl(PatientDischargeRepository patientDischargeRepository, PatientDischargeMapper patientDischargeMapper) {
        this.patientDischargeRepository = patientDischargeRepository;
        this.patientDischargeMapper = patientDischargeMapper;
    }

    @Override
    public Optional<PatientDischargeBo> getPatientDischarge(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        PatientDischargeBo patientBo = new PatientDischargeBo();
        patientDischargeRepository.findById(internmentEpisodeId).ifPresent(patient -> {
            patientBo.setInternmentEpisodeId(patient.getInternmentEpisodeId());
            patientBo.setDischargeTypeId(patient.getDischargeTypeId());
            patientBo.setAdministrativeDischargeDate(patient.getAdministrativeDischargeDate());
            patientBo.setMedicalDischargeDate(patient.getMedicalDischargeDate());
        });
        Optional<PatientDischargeBo> result = Optional.of(patientBo);
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

}
