package ar.lamansys.sgh.shared.infrastructure.input.service.events;

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

	public void publish(Integer patientId, String topic) {
		String fullTopic = "HSI/" + namePrefix + "/" + topic;
		String message = getSimplePayload(patientId, fullTopic);
		MqttMetadataDto mqttMetadataDto = MqttDtoUtils.getMqtMetadataDto(fullTopic, message);
		mqttCallExternalService.publish(mqttMetadataDto);
	}

	public void llamadorPublish(String topic, NotifyPatientBo notifyPatientBo) {
		String fullTopic = "HSI/" + namePrefix + "/" + topic + "/" +notifyPatientBo.getTopic();
		notifyPatientBo.setTopic(fullTopic);
		mqttCallExternalService.publish(mapTo(notifyPatientBo));
	}

	private MqttMetadataDto mapTo(NotifyPatientBo notifyPatientBo) {
		return new MqttMetadataDto(notifyPatientBo.getTopic(), getMessage(notifyPatientBo), false, 2, "add");
	}

	protected String getMessage(NotifyPatientBo notifyPatientBo) {
		return String.format("\"data\":{\"appointmentId\":%s,\"patient\":\"%s\",\"sector\":%s,\"doctor\":\"%s\",\"doctorsOffice\":\"%s\"}", notifyPatientBo.getAppointmentId(), notifyPatientBo.getPatientName(), notifyPatientBo.getSectorId(), notifyPatientBo.getDoctorName(), notifyPatientBo.getDoctorsOfficeName());
	}


	private String getSimplePayload(Integer patientId, String topic) {
		return String.format("\"description\":\"{\\\"patientId\\\":%d,\\\"topic\\\":\\\"%s\\\"}\"", patientId, topic);
	}


}
