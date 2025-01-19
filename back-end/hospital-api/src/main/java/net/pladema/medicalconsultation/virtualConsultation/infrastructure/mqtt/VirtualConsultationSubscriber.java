package net.pladema.medicalconsultation.virtualConsultation.infrastructure.mqtt;

import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.mqtt.infraestructure.configuration.webSocket.QueueListener;
import ar.lamansys.mqtt.infraestructure.input.service.MqttCallExternalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationEventBo;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationEventDto;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@AllArgsConstructor
@Slf4j
@Configuration
public class VirtualConsultationSubscriber {

	private final MqttCallExternalService mqttCallExternalService;

	private final SimpMessagingTemplate template;

	private final QueueListener queueListener;

	private final ObjectMapper objectMapper;

	@Bean
	public void subscribe() throws MqttException {
		mqttCallExternalService.subscribe(null, "/HSI/VIRTUAL-CONSULTATION/NOTIFY", getQueueListeners());
		mqttCallExternalService.subscribe(null, "/HSI/VIRTUAL-CONSULTATION/CHANGE-RESPONSIBLE-STATE", handleTopicState("virtual-consultation-responsible-state-change"));
		mqttCallExternalService.subscribe(null, "/HSI/VIRTUAL-CONSULTATION/CHANGE-VIRTUAL-CONSULTATION-STATE", handleTopicState("virtual-consultation-state-change"));
		mqttCallExternalService.subscribe(null, "/HSI/VIRTUAL-CONSULTATION/NEW-VIRTUAL-CONSULTATION", handleTopicState("new-virtual-consultation"));
		mqttCallExternalService.subscribe(null, "/HSI/VIRTUAL-CONSULTATION/CHANGE-PROFESSIONAL-STATE", handleTopicState("virtual-consultation-professional-state-change"));
		mqttCallExternalService.subscribe(null, "/HSI/VIRTUAL-CONSULTATION/CHANGE-RESPONSIBLE-PROFESSIONAL", handleTopicState("virtual-consultation-responsible-professional-change"));
	}

	private List<Consumer<MqttMetadataBo>> handleTopicState(String webSocketPathDestination) {
		return new ArrayList<>(List.of(m -> {
			try {
				SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
				headerAccessor.setLeaveMutable(true);
				template.convertAndSend("/topic/" + webSocketPathDestination, m.getMessage(), headerAccessor.getMessageHeaders());
			} catch (Throwable e) {
				log.error(e.getMessage(), e);
			}
		}));
	}

	private List<Consumer<MqttMetadataBo>> getQueueListeners() {
		return new ArrayList<>(List.of(m -> {
			try {
				VirtualConsultationEventBo data = objectMapper.readValue(m.getMessage(), VirtualConsultationEventBo.class);
				String destinationSessionId = getDestinationSessionId(data.getDestinationUserId());
				if (destinationSessionId != null) {
					SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
					headerAccessor.setSessionId(destinationSessionId);
					headerAccessor.setLeaveMutable(true);
					VirtualConsultationEventDto message = new VirtualConsultationEventDto(data);
					template.convertAndSendToUser(destinationSessionId, "/queue/" + data.getEvent().getWsPath(), objectMapper.writeValueAsString(message), headerAccessor.getMessageHeaders());
				}
			}
			catch (Throwable e) {
				log.error(e.getMessage(), e);
			}
		}));
	}

	private String getDestinationSessionId(Integer userId) {
		for (Map.Entry<String, Integer> entry: queueListener.getActiveUserSessions().entrySet())
			if (userId.equals(entry.getValue()))
				return entry.getKey();
		return null;
	}

}
