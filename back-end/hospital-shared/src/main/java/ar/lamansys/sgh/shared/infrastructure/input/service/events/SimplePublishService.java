package ar.lamansys.sgh.shared.infrastructure.input.service.events;

import ar.lamansys.mqtt.infraestructure.input.MqttDtoUtils;
import ar.lamansys.mqtt.infraestructure.input.rest.dto.MqttMetadataDto;
import ar.lamansys.mqtt.infraestructure.input.service.MqttCallExternalService;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.MqttTypeBo;

public class SimplePublishService {

	private final MqttCallExternalService mqttCallExternalService;
	private final String namePrefix;

	public SimplePublishService(MqttCallExternalService mqttCallExternalService, String namePrefix) {
		this.mqttCallExternalService = mqttCallExternalService;
		this.namePrefix = namePrefix;
	}

	public void publish(Integer patientId, Integer institutionId, String topic) {
		String fullTopic = "/HSI/INSTITUTION/"+ institutionId + "/" + namePrefix + "/" +  topic;
		String message = getSimplePayload(patientId, fullTopic);
		MqttMetadataDto mqttMetadataDto = MqttDtoUtils.getMqtMetadataDto(fullTopic, message);
		mqttCallExternalService.publish(mqttMetadataDto);
	}

	public void genericPublish(String topic, String message) {
		String fullTopic = "/HSI/" + namePrefix + "/" +  topic;
		MqttMetadataDto mqttMetadataDto = MqttDtoUtils.getMqtMetadataDto(fullTopic, message);
		mqttCallExternalService.publish(mqttMetadataDto);
	}

	public void appointmentCallerPublish(String topic, NotifyPatientDto notifyPatientDto, Integer institutionId) {
		String fullTopic = callerTopic(topic, institutionId, notifyPatientDto.getTvMonitor());
		mqttCallExternalService.publish(mapTo(fullTopic, getMessage(notifyPatientDto)));
	}

	public void emergencyCareCallerPublish(String topic, EmergencyCareEpisodeNotificationDto emergencyCareEpisodeNotificationDto, Integer institutionId) {
		String fullTopic = callerTopic(topic, institutionId, emergencyCareEpisodeNotificationDto.getTvMonitor());
		mqttCallExternalService.publish(mapTo(fullTopic, getEmergencyCareSchedulerMessage(emergencyCareEpisodeNotificationDto)));
	}

	private String callerTopic(String topicCall, Integer institutionId, String tvMonitor) {
		return "/HSI/" + namePrefix + "/" + topicCall + "/" + institutionId + "/" + tvMonitor;
	}

	private String getEmergencyCareSchedulerMessage(EmergencyCareEpisodeNotificationDto notification) {
		return String.format("{\"type\":\"%s\"," + "\"data\":{\"id\":%s,\"patient\":\"%s\",\"doctor\":\"%s\",\"place\":\"%s\",\"triageCategory\":{\"name\": \"%s\", \"colorCode\": \"%s\"}}}",
				MqttTypeBo.ADD.getId(), notification.getEpisodeId(), notification.getPatientName(), notification.getDoctorName(), notification.getPlaceName(), notification.getTriageCategory().getName(), notification.getTriageCategory().getColorCode());
	}

	private MqttMetadataDto mapTo(String topic, String message) {
		return  MqttDtoUtils.getMqtMetadataDto(topic, message);
	}

	protected String getMessage(NotifyPatientDto notifyPatientDto) {
		return String.format("{\"type\":\"%s\"," +
				"\"data\":{\"id\":%s,\"patient\":\"%s\",\"sector\":%s,\"doctor\":\"%s\",\"place\":\"%s\"}}",MqttTypeBo.ADD.getId(), notifyPatientDto.getAppointmentId(), notifyPatientDto.getPatientName(), notifyPatientDto.getSectorId(), notifyPatientDto.getDoctorName(), notifyPatientDto.getDoctorsOfficeName());
	}


	private String getSimplePayload(Integer patientId, String topic) {
		return String.format("{\"description\":\"{\\\"patientId\\\":%d,\\\"topic\\\":\\\"%s\\\"}\"}", patientId, topic);
	}


}
