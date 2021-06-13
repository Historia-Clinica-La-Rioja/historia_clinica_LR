package net.pladema.hl7.dataexchange.model.adaptor;

import lombok.Getter;
import org.hl7.fhir.r4.model.Address;

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

    public FhirAddress setAll(Object[] tuple){
        int index=0;
        setAddress(Cast.toString(tuple[index++]), Cast.toString(tuple[index++]),
                Cast.toString(tuple[index++]), Cast.toString(tuple[index++])
        )
                .setPostcode(Cast.toString(tuple[index++]))
                .setCity(Cast.toString(tuple[index++]))
                .setProvince(Cast.toString(tuple[index++]))
                .setCountry(Cast.toString(tuple[index]));
        return this;
    }

    public FhirAddress setAddress(String street, String number, String apartment, String floor){
        this.address = FhirString.joining(street, number, apartment, floor);
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
        return FhirString.hasText(address);
    }

    public String getAddress(){
        return hasAddressData() ? address : null;
    }
}
