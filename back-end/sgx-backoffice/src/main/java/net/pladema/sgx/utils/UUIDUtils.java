package net.pladema.sgx.utils;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generación de diferentes códigos aleatorios
 */
public class UUIDUtils {
	private static final Logger LOG = LoggerFactory.getLogger(UUIDUtils.class);
	private static Random _rand = null;

	private static Random getRandom() {
		if (_rand == null) {
			_rand = new Random();
		}
		return _rand;
	}

	public static String generateErrorTrace() {
		// un número random, corto como para buscar en los logs
		return String.format("%06d", getRandom().nextInt(999999));
	}

	public static String generateCheckInCode(String prefix) {
		return prefix + UUID.randomUUID().toString();
	}
}
