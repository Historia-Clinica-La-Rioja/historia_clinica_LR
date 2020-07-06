package net.pladema.address.controller.service.domain;

import lombok.Getter;
import net.pladema.address.controller.dto.CityDto;
import net.pladema.address.repository.domain.AddressVo;

@Getter
public class AddressBo {

    private Integer id;

    private String street;

    private String number;

    private String floor;

    private String apartment;

    private CityDto city;

    public AddressBo (AddressVo addressVo){
        this.id = addressVo.getId();
        this.street = addressVo.getStreet();
        this.number = addressVo.getNumber();
        this.floor = addressVo.getFloor();
        this.apartment = addressVo.getApartment();
        this.city = addressVo.getCity();
    }
}
