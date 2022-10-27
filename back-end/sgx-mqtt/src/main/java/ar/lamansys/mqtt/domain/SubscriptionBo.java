package ar.lamansys.mqtt.domain;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ar.lamansys.mqtt.domain.exception.SubscriptionBoException;
import ar.lamansys.mqtt.domain.exception.SubscriptionBoExceptionEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class SubscriptionBo {
	@EqualsAndHashCode.Include
	private final String topic;
	private List<ActionBo> actions;
	public SubscriptionBo(Integer userId, String topic, List<Consumer<MqttMetadataBo>> consumers) {
		Objects.requireNonNull(topic, () -> {
			throw new SubscriptionBoException(SubscriptionBoExceptionEnum.NULL_TOPIC, "El topico es obligatorio");
		});
		this.topic = topic;
		this.actions = consumers.stream()
				.map(mqttMetadataBoConsumer -> new ActionBo(userId, mqttMetadataBoConsumer))
				.collect(Collectors.toList());
	}

	public boolean apply(String topic) {
		String topicExp = this.topic//
				.replaceAll("\\$", "\\\\\\$")//
				.replaceAll("\\+", "[^/]+")//
				.replaceAll("/\\#$", "(\\$|/.+)") // FINISH TOPIC WITH #
				.replaceAll("\\#", "(\\|.+)"); // SUSCRIBE TO #
		Pattern pattern = Pattern.compile(topicExp);
		return pattern.matcher(topic).matches();
	}


	public void consume(MqttMetadataBo message) {
		actions.forEach(c -> c.consume(message));
	}

	public void addActions(List<ActionBo> actions) {
		this.actions.addAll(actions);
	}

	public void removeActionsFromUser(Integer userId) {
		this.actions = actions.stream()
				.filter(actionBo -> !actionBo.matchUser(userId))
				.collect(Collectors.toList());
	}

	public boolean isEmpty() {
		return actions.isEmpty();
	}
}
