package ar.lamansys.sgx.cubejs.infrastructure.repository.permissions;

import lombok.Getter;

@Getter
public class DashboardRoleInfoDto {

    private Short id;

    private Integer institution;

    private String value;

    public DashboardRoleInfoDto(Short id, Integer institution, String value) {
        this.id = id;
        this.institution = institution;
        this.value = value;
    }
}