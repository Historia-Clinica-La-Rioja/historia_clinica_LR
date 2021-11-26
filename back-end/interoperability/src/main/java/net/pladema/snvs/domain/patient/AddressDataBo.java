package net.pladema.snvs.domain.patient;

import lombok.Getter;

@Getter
public class AddressDataBo {

    private final String street;

    private final Integer departmentId;

    private final Integer cityId;

    private final Short provinceId;

    private final Integer countryId;

    public AddressDataBo(String street, Integer departmentId,
                         Integer cityId, Short provinceId, Integer countryId) {
        this.street = street;
        this.departmentId = departmentId;
        this.cityId = cityId;
        this.provinceId = provinceId;
        this.countryId = countryId;
    }

}
