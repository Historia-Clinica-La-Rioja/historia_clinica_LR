package net.pladema.hsi.extensions.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

}
