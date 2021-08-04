package ar.lamansys.odontology.domain;

public enum ESurfacePosition {

    INTERNAL("internal"),
    EXTERNAL("external"),
    LEFT("left"),
    RIGHT("right"),
    CENTRAL("central");

    private final String value;

    ESurfacePosition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
