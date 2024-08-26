package net.pladema.establishment.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

	private String provinceCode;

}
