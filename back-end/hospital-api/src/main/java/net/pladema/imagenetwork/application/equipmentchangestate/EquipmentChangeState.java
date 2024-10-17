package net.pladema.imagenetwork.application.equipmentchangestate;

import ar.lamansys.mqtt.application.ports.MqttClientService;
import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.HistoricAppointmentStateRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.AppointmentValidatorService;
import net.pladema.medicalconsultation.appointment.service.EquipmentAppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class EquipmentChangeState {

    private final AppointmentService appointmentService;
    private final EquipmentAppointmentService equipmentAppointmentService;
    private final AppointmentValidatorService appointmentValidatorService;
    private final MqttClientService mqttClientService;
    private final HistoricAppointmentStateRepository historicAppointmentStateRepository;

    @Transactional
    public boolean run(Integer institutionId, Integer appointmentId, Short stateId, String reason) {
        log.debug("Input parameters -> institutionId {}, appointmentId {}", institutionId, appointmentId);
        boolean result = false;

        boolean isValidTransitionAppointmentState = appointmentValidatorService.validateStateUpdate(institutionId, appointmentId, stateId, reason);
        if (isValidTransitionAppointmentState)
            result = appointmentService.updateState(appointmentId, stateId, UserInfo.getCurrentAuditor(), reason);

        this.publishWorklist(institutionId, appointmentId, stateId);

        log.debug("Output -> {}", result);
        return result;
    }

    private void publishWorklist(Integer institutionId, Integer appointmentId, Short stateId) {

        boolean hasAlreadyPublishedWorkList = historicAppointmentStateRepository.hasHistoricallyConfirmedAtLeastOnes(appointmentId);
        if (hasAlreadyPublishedWorkList) {
            log.warn("HSI-12356 Not publishWorkList because hasAlreadyPublishedWorkList -> institutionId {}, appointmentId {}, stateId {}", institutionId, appointmentId, stateId);
            return;
        }

        boolean isNotNecessaryToPublishWorklist = AppointmentState.SERVED == stateId;
        if (isNotNecessaryToPublishWorklist) {
            log.debug("HSI-12356 Not publishWorkList -> appointment {} was served", appointmentId);
            return;
        }

        MqttMetadataBo data = equipmentAppointmentService.setToPublishWorkList(institutionId, appointmentId);
        mqttClientService.publish(data);
    }
}
