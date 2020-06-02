package net.pladema.featureflags.service.domain;

import java.util.Arrays;
import java.util.Optional;

public enum FlavorBo {

    TANDIL("tandil"),
    HOSPITALES("minsal");

    private final String text;
    
    public static final boolean ALL  = true;
    public static final boolean NONE = false;

    FlavorBo(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }

    public static FlavorBo getEnum(String text) {
        Optional<FlavorBo> found = find(text);
        if (found.isPresent()) {
            return found.get();
        }
        return found.orElseThrow(IllegalArgumentException::new);
    }

    public static Optional<FlavorBo> find(String text) {
        for (FlavorBo e : FlavorBo.values()) {
            if (e.text.equalsIgnoreCase(text)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    /**
     * Verifica que este sabor esté en la lista
     * @param flavors
     * @return true solo si _este_ está en la lista
     */
    public boolean anyMatch(FlavorBo...flavors) {
        return Arrays.stream(flavors).anyMatch(getEnum(text)::equals);
    }

    /**
     * Verifica que este sabor NO esté en la lista.
     * Se podría usar ! pero así permite mejorar la legibilidad en la definición.
     * @param flavors
     * @return true siempre que _este_ no esté en la lista
     */
    public boolean wontMatch(FlavorBo...flavors) {
        return !anyMatch(flavors);
    }
}
