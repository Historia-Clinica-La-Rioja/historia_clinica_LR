package net.pladema.hsi.extensions.configuration.features;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Los módulos pueden implementar este servicio para que se incluya en el menú de la aplicación un acceso
 * para ver el estado del módulo. Útil para mostrar configuración y estado de la integración con servicios
 * externos.
 *
 * Este servicio no utiliza el proveedor de statusData sincrónicamente, sinó que para evitar sobrecargar el
 * servicio externo, este se actualiza cada cierto tiempo siempre y cuando alguien haya intentado ver el
 * último estado.
 *
 */
@Slf4j
public class FeatureStatusService {
	public static final Supplier<Map<String, Object>> FEATURE_DISABLED = () -> Collections.emptyMap();
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final String prefix;
	private final Supplier<Map<String, Object>> statusData;
	private final Supplier<List<FeatureProperty>> properties;

	private boolean needToUpdateLastStatusData = true;
	private Map<String, Object> lastStatusData = null;

	public FeatureStatusService(
			String prefix,
			Supplier<List<FeatureProperty>> properties,
			Supplier<Map<String, Object>> statusData
	) {
		this.prefix = prefix;
		this.properties = properties;
		this.statusData = statusData;
	}


	public FeatureStatus status() {
		return new FeatureStatus(
				prefix,
				properties.get(),
				lastStatusData()
		);
	}

	private Map<String, Object> lastStatusData() {
		needToUpdateLastStatusData = true;
		return lastStatusData;
	}

	@Scheduled(fixedRate = 5000)
	public void scheduleUpdateLastStatusData() {
		if (needToUpdateLastStatusData) {
			try {
				lastStatusData = statusData.get();
			} catch (Exception e) {
				lastStatusData = Map.of(
						"error", e.getMessage(),
						"cause", formatCause(e.getCause())
				);
			}
		}
		needToUpdateLastStatusData = false;
	}

	private static Object formatCause(Throwable cause) {
		try {
			return MAPPER.readValue(MAPPER.writeValueAsString(cause), Object.class);
		} catch (JsonProcessingException e) {
			log.debug("Couldn't format cause", cause);
		}
		return "-no se pudo formatear la causa-";
	}
}
