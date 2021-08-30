package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Getter;

@Getter
public class RoleInfoDto {

    private Short id;

    private Integer institution;

    private String value;

    public RoleInfoDto(Short id, Integer institution, String value) {
        this.id = id;
        this.institution = institution;
        this.value = value;
    }
}
