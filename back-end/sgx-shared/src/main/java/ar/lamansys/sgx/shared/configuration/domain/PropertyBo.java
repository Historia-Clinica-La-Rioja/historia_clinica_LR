package ar.lamansys.sgx.shared.configuration.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PropertyBo {

    @ToString.Include
    private final String id;

    @ToString.Include
    private final String value;

    @ToString.Include
    private final String origin;

    public PropertyBo(String id, String value, String origin) {
        this.id = id;
        this.value = value;
        this.origin = origin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertyBo that = (PropertyBo) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
