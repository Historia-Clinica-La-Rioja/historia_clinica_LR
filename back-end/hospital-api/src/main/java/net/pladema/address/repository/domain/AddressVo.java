package net.pladema.address.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pladema.address.controller.dto.CityDto;

@AllArgsConstructor
@Getter
public class AddressVo {
    private Integer id;

    private String street;

    private String number;

    private String floor;

    private String apartment;

    private String postcode;

    private CityDto city;

    public AddressVo (Integer addressId,String street, String number, String floor, String apartment,String postcode, Integer cityId,String cityDescription){
        this.id = addressId;
        this.street = street;
        this.number = number;
        this.floor = floor;
        this.apartment = apartment;
        this.postcode = postcode;
        this.city = new CityDto();
        this.city.setId(cityId);
        this.city.setDescription(cityDescription);
    }
}
