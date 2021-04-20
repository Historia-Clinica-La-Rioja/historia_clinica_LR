package net.pladema.hsi.extensions.utils;

import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonResourceUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonResourceUtils.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static <T> T readJson(String resourceLocation, TypeReference<? extends T> valueTypeRef, T empty) {
		try {
			Path file = ResourceUtils.getFile(resourceLocation).toPath();
			return MAPPER.readValue(file.toFile(), valueTypeRef);
		} catch (IOException e) {
			LOGGER.info("No se pudo leer JSON en '{}', mensaje: {}", resourceLocation, e.getMessage());
			return empty;
		}
	}

}
