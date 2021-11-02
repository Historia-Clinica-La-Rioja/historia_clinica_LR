package ar.lamansys.immunization.domain.user;

import lombok.Getter;

import java.util.List;

@Getter
public class RoleInfoBo {

    private Short id;

    private Integer institution;

    private String value;

    public RoleInfoBo(Short id, Integer institution, String value) {
        this.id = id;
        this.institution = institution;
        this.value = value;
    }

    public boolean anyRole(List<String> roles) {
        return !roles.isEmpty() && roles.contains(value);
    }
}
