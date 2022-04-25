package net.pladema.clinichistory.hospitalization.service.patientdischarge;

import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientDischargeServiceImpl implements PatientDischargeService {

    private static final Logger LOG = LoggerFactory.getLogger(PatientDischargeServiceImpl.class);

    public static final String INPUT_PARAMETERS_INTERNMENT_EPISODE_ID = "Input parameters -> internmentEpisodeId {}";
    private static final String LOGGING_OUTPUT = "Output -> {}";

    private final PatientDischargeRepository patientDischargeRepository;

    public PatientDischargeServiceImpl(PatientDischargeRepository patientDischargeRepository) {
        this.patientDischargeRepository = patientDischargeRepository;
    }

    @Override
    public Optional<PatientDischargeBo> getPatientDischarge(Integer internmentEpisodeId) {
        LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        PatientDischargeBo patientBo = new PatientDischargeBo();
        patientDischargeRepository.findById(internmentEpisodeId).ifPresent(patient -> {
            patientBo.setInternmentEpisodeId(patient.getInternmentEpisodeId());
            patientBo.setDischargeTypeId(patient.getDischargeTypeId());
            patientBo.setAdministrativeDischargeDate(patient.getAdministrativeDischargeDate());
            patientBo.setMedicalDischargeDate(patient.getMedicalDischargeDate());
			patientBo.setPhysicalDischargeDate(patient.getPhysicalDischargeDate());
        });
        Optional<PatientDischargeBo> result = Optional.of(patientBo);
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public boolean existsById(Integer internmentEpisodeId) {
        LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        boolean result = patientDischargeRepository.existsById(internmentEpisodeId);
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

}
