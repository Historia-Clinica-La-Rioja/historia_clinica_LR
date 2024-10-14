package net.pladema.emergencycare.application;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EPatientType;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;
import net.pladema.emergencycare.service.EmergencyCareEpisodeDischargeService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeStateService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import net.pladema.emergencycare.service.domain.PatientECEBo;
import net.pladema.events.EHospitalApiTopicDto;
import net.pladema.events.HospitalApiPublisher;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateMedicalDischarge {

    private final EmergencyCareEpisodeDischargeService emergencyCareEpisodeDischargeService;
    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
    private final PatientExternalService patientExternalService;
    private final EmergencyCareEpisodeService emergencyCareEpisodeService;
    private final EmergencyCareEpisodeStateService emergencyCareEpisodeStateService;
    private final HospitalApiPublisher hospitalApiPublisher;

    @Transactional
    public boolean run(MedicalDischargeBo medicalDischargeBo) {
        log.debug("Input parameters -> medicalDischargeBo {}", medicalDischargeBo);

        Integer episodeId = medicalDischargeBo.getSourceId();
        Integer institutionId = medicalDischargeBo.getInstitutionId();
        this.assertPatientIsNotTemporary(episodeId);

        this.setValuesFromEpisodeAndInstitution(medicalDischargeBo, episodeId, institutionId);

        boolean saved = emergencyCareEpisodeDischargeService.newMedicalDischarge(medicalDischargeBo, institutionId);
        emergencyCareEpisodeStateService.changeState(episodeId, institutionId, EmergencyCareState.CON_ALTA_MEDICA, null, null, null);
        hospitalApiPublisher.publish(medicalDischargeBo.getPatientId(), institutionId, EHospitalApiTopicDto.ALTA_MEDICA);

        log.debug("Output -> {}", saved);
        return saved;
    }

    private void setValuesFromEpisodeAndInstitution(MedicalDischargeBo medicalDischargeBo, Integer episodeId, Integer institutionId) {
        Integer medicalDischargeBy = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        medicalDischargeBo.setMedicalDischargeBy(medicalDischargeBy);
        EmergencyCareBo emergencyCareBo = emergencyCareEpisodeService.get(episodeId, institutionId);
        Integer patientId = emergencyCareBo.getPatient().getId();
        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        medicalDischargeBo.setPatientInfo(patientInfo);
        medicalDischargeBo.setPatientId(patientId);
    }

    private void assertPatientIsNotTemporary(Integer episodeId) {
        PatientECEBo patientData = emergencyCareEpisodeService.getRelatedPatientData(episodeId);
        if (EPatientType.EMERGENCY_CARE_TEMPORARY.getId().equals(patientData.getTypeId()))
            throw new IllegalStateException("No se puede dar el alta a un paciente temporal de guardia");
    }
}
