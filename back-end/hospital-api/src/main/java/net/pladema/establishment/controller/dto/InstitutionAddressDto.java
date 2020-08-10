package net.pladema.establishment.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.dto.CityDto;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class InstitutionAddressDto implements Serializable {

    private Integer addressId;
    private String street;
    private String number;
    private String floor;
    private String apartment;
    private CityDto city;

    public InstitutionAddressDto (AddressDto addressDto) {
        this.addressId = addressDto.getId();
        this.street = addressDto.getStreet();
        this.number = addressDto.getNumber();
        this.floor = addressDto.getFloor();
        this.apartment = addressDto.getApartment();
        this.city = addressDto.getCity();
    }

}
