package ar.lamansys.sgh.shared.infrastructure.input.service.events;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.MqttTypeBo;
import ar.lamansys.mqtt.infraestructure.input.MqttDtoUtils;
import ar.lamansys.mqtt.infraestructure.input.rest.dto.MqttMetadataDto;
import ar.lamansys.mqtt.infraestructure.input.service.MqttCallExternalService;

public class SimplePublishService {

	private final MqttCallExternalService mqttCallExternalService;
	private final String namePrefix;

	public SimplePublishService(MqttCallExternalService mqttCallExternalService, String namePrefix) {
		this.mqttCallExternalService = mqttCallExternalService;
		this.namePrefix = namePrefix;
	}

	public void publish(Integer patientId, Integer institutionId, String topic) {
		String fullTopic = "HSI/INSTITUTION/"+ institutionId + "/" + namePrefix + "/" +  topic;
		String message = getSimplePayload(patientId, fullTopic);
		MqttMetadataDto mqttMetadataDto = MqttDtoUtils.getMqtMetadataDto(fullTopic, message);
		mqttCallExternalService.publish(mqttMetadataDto);
	}

	public void appointmentCallerPublish(String topic, NotifyPatientDto notifyPatientDto) {
		String fullTopic = "HSI/" + namePrefix + "/" + topic + "/" + notifyPatientDto.getTopic();
		notifyPatientDto.setTopic(fullTopic);
		mqttCallExternalService.publish(mapTo(notifyPatientDto));
	}

	private MqttMetadataDto mapTo(NotifyPatientDto notifyPatientDto) {
		return  MqttDtoUtils.getMqtMetadataDto(notifyPatientDto.getTopic(), getMessage(notifyPatientDto));
	}

	protected String getMessage(NotifyPatientDto notifyPatientDto) {
		return String.format("{\"type\":\"%s\"," +
				"\"data\":{\"appointmentId\":%s,\"patient\":\"%s\",\"sector\":%s,\"doctor\":\"%s\",\"doctorsOffice\":\"%s\"}}",MqttTypeBo.ADD.getId(), notifyPatientDto.getAppointmentId(), notifyPatientDto.getPatientName(), notifyPatientDto.getSectorId(), notifyPatientDto.getDoctorName(), notifyPatientDto.getDoctorsOfficeName());
	}


	private String getSimplePayload(Integer patientId, String topic) {
		return String.format("{\"description\":\"{\\\"patientId\\\":%d,\\\"topic\\\":\\\"%s\\\"}\"}", patientId, topic);
	}


}
