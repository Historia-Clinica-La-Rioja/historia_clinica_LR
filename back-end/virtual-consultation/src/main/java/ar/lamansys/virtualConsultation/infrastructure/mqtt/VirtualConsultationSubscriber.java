package ar.lamansys.virtualConsultation.infrastructure.mqtt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.mqtt.infraestructure.configuration.webSocket.QueueListener;
import ar.lamansys.mqtt.infraestructure.input.service.MqttCallExternalService;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class VirtualConsultationSubscriber {

	private final MqttCallExternalService mqttCallExternalService;

	private final SimpMessagingTemplate template;

	private final QueueListener queueListener;

	@Bean
	public void subscribe() throws MqttException {
		mqttCallExternalService.subscribe(null, "HSI/VIRTUAL-CONSULTATION/NOTIFY", getQueueListeners());
	}

	private List<Consumer<MqttMetadataBo>> getQueueListeners() {
		return new ArrayList<>(List.of(m -> {
			try {
				String destinationSessionId = getDestinationSessionId(m.getMessage());
				if (destinationSessionId != null) {
					SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
					headerAccessor.setSessionId(destinationSessionId);
					headerAccessor.setLeaveMutable(true);
					template.convertAndSendToUser(destinationSessionId, "/queue/virtual-consultation-notification", m.getMessage(), headerAccessor.getMessageHeaders());
				}
			}
			catch (Throwable e) {
				e.printStackTrace();
			}
		}));
	}

	private String getDestinationSessionId(String message) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(message);
		if (!jsonNode.has("targetId"))
			throw new IllegalStateException("TargetId is needed");
		Integer userId = jsonNode.get("targetId").asInt();
		for (Map.Entry<String, Integer> entry: queueListener.getActiveUserSessions().entrySet())
			if (userId.equals(entry.getValue()))
				return entry.getKey();
		return null;
	}

}
