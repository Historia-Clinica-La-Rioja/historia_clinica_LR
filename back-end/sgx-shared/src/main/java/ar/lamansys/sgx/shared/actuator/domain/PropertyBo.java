package ar.lamansys.sgx.shared.actuator.domain;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class PropertyBo {

    @ToString.Include
    private Integer id;

    @ToString.Include
    private String property;

    @ToString.Include
    private String description;

    @ToString.Include
    private String value;

    @ToString.Include
    private String origin;

    @ToString.Include
    private String nodeId;

    private LocalDateTime updatedOn;

    public PropertyBo(Integer id, String property, String value, String description, String origin, String nodeId, LocalDateTime updatedOn) {
        this.id = id;
        this.property = property;
        this.value = value;
        this.description = description;
        this.nodeId = nodeId;
        this.origin = origin;
        this.updatedOn = updatedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertyBo that = (PropertyBo) o;

        if (!property.equals(that.property)) return false;
        return nodeId.equals(that.nodeId);
    }

    @Override
    public int hashCode() {
        int result = property.hashCode();
        result = 31 * result + nodeId.hashCode();
        return result;
    }
}
