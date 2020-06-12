package net.pladema.featureflags.service.domain;

import java.util.Optional;

public enum FlavorBo {

    TANDIL("tandil"),
    CHACO("chaco"),
    HOSPITALES("minsal");

    private final String text;

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


}
