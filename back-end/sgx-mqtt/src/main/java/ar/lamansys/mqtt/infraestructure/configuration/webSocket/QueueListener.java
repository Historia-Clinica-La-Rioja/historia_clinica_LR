package ar.lamansys.mqtt.infraestructure.configuration.webSocket;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import ar.lamansys.sgx.shared.auth.user.SgxUserDetails;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QueueListener {

	public final Map<String, Integer> usersConnected = new HashMap<>();

	public Map<String, Integer> getActiveUserSessions() {
		return usersConnected;
	}

	@EventListener
	public void onSessionConnectedEvent(SessionConnectedEvent event) {
		log.debug("User {} connected", event.getUser());
		String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
		Integer userId = ((SgxUserDetails) ((UsernamePasswordAuthenticationToken) Objects.requireNonNull(event.getMessage().getHeaders().get("simpUser"))).getPrincipal()).getUserId();
		usersConnected.put(sessionId, userId);
	}

	@EventListener
	public void onSessionSubscribeEvent(SessionSubscribeEvent event) throws MqttException {
		log.debug("Event {}", event);
	}

	@EventListener
	public void onDisconnectEvent(SessionDisconnectEvent event) {
		log.debug("User with session id {} disconnected", event.getSessionId());
		String sessionId = event.getSessionId();
		usersConnected.remove(sessionId);
	}

}
