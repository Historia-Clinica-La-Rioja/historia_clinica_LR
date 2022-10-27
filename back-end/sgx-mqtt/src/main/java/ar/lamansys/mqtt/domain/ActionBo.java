package ar.lamansys.mqtt.domain;

import java.util.Objects;
import java.util.function.Consumer;

import ar.lamansys.mqtt.domain.exception.SubscriptionBoException;
import ar.lamansys.mqtt.domain.exception.SubscriptionBoExceptionEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class ActionBo {
	@EqualsAndHashCode.Include
	private final Integer userId;
	private final Consumer<MqttMetadataBo> action;
	public ActionBo(Integer userId, Consumer<MqttMetadataBo> action) {
		this.userId = userId;
		if (action == null)
			throw new SubscriptionBoException(SubscriptionBoExceptionEnum.EMPTY_LIST, "Al menos 1 acción es requerida");
		this.action = action;
	}
	public void consume(MqttMetadataBo message) {
		action.accept(message);
	}

	public boolean matchUser(Integer userId) {
		return Objects.equals(userId, userId);
	}
}
