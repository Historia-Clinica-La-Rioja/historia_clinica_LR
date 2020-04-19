package net.pladema.address.controller.dto;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressDto implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5023340278494944407L;

    private Integer id;

    private String street;

    private String number;

    private String floor;

    private String apartment;

    private String quarter;

    private Integer cityId;

    private String postcode;
    
    public static AddressDto buildDummy() {
		AddressDto newAddress = new AddressDto();
		newAddress.setStreet("");
		newAddress.setNumber("");
		newAddress.setCityId(1);
		newAddress.setPostcode("");
		return newAddress;
    }

}
