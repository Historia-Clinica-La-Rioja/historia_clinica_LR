package net.pladema.hl7.dataexchange.model.adaptor;

import lombok.Getter;
import org.hl7.fhir.r4.model.Address;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class FhirAddress {

    private final Address.AddressUse addressUse;

    public FhirAddress(Address.AddressUse addressUse){
        super();
        this.addressUse = addressUse;
    }

    private String address;
    private String city;
    private String postcode;
    private String province;
    private String country;

    public FhirAddress setAddress(String street, String number, String appartment, String floor){
        this.address = FhirString.joining(street, number, appartment, floor);
        return this;
    }

    public FhirAddress setAddress(String fullAddress){
        this.address=fullAddress;
        return this;
    }

    public FhirAddress setCity(String city){
        if(city == null)
            return this;
        this.city=city;
        return this;
    }

    public FhirAddress setPostcode(String postcode){
        if(postcode == null)
            return this;
        this.postcode=postcode;
        return this;
    }

    public FhirAddress setProvince(String province){
        if(province == null)
            return this;
        this.province=province;
        return this;
    }

    public FhirAddress setCountry(String country) {
        if(country == null)
            return this;
        this.country = country;
        return this;
    }

    public boolean hasAddressData(){
        return address != null && !address.isBlank();
    }
}
