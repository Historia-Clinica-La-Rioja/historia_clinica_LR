package net.pladema.establishment.service.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstitutionBo {
    private Integer id;

    private String name;

    private Integer addressId;

    private String website;

    private String phone;

    private String email;

    private String cuit;

    private String sisaCode;

    private String timezone;

}
