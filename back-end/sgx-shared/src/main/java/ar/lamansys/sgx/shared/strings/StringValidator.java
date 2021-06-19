package ar.lamansys.sgx.shared.strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringValidator {

    private static final Logger LOG = LoggerFactory.getLogger(StringValidator.class);

    public void validateMaxLength(String s, Integer maxLength) {
        LOG.debug("Input -> s: {}, maxLength: {}", s, maxLength);
        if (s != null && s.length() > maxLength)
            throw new StringValidatorException("El texto debe tener como máximo " + maxLength + " caracteres.");
    }
}
