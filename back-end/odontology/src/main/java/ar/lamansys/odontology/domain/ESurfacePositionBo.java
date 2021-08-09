package ar.lamansys.odontology.domain;

public enum ESurfacePositionBo {

    INTERNAL("internal"),
    EXTERNAL("external"),
    LEFT("left"),
    RIGHT("right"),
    CENTRAL("central");

    private final String value;

    ESurfacePositionBo(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
