package net.pladema.hsi.extensions.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;

public class JsonResourceUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonResourceUtils.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static <T> T readJson(String resourceLocation, TypeReference<? extends T> valueTypeRef, T empty) {
		try {
			Resource resource = new ClassPathResource(resourceLocation);
			return MAPPER.readValue(resource.getInputStream(), valueTypeRef);
		} catch (Exception e) {
			LOGGER.info("No se pudo leer JSON en '{}', mensaje: {}", resourceLocation, e.getMessage());
			return empty;
		}
	}

	public static <T> T readJson(InputStream inputStream, Class<T> valueType, T empty) {
		try {
			return MAPPER.readValue(inputStream, valueType);
		} catch (Exception e) {
			LOGGER.info("No se pudo leer JSON, mensaje: {}",  e.getMessage());
			return empty;
		}
	}

}
