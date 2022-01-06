package ar.lamansys.sgx.shared.flavor;

public enum FlavorBo {

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

}
