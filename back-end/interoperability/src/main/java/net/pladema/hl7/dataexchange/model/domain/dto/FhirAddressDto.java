package net.pladema.hl7.dataexchange.model.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;

@NoArgsConstructor
@Getter
public class FhirAddressDto {
    public FhirAddressDto(FhirAddress fhirAddress){
        if(fhirAddress != null) {
            this.address = fhirAddress.getAddress();
            this.city = fhirAddress.getCity();
            this.postcode = fhirAddress.getPostcode();
            this.province = fhirAddress.getProvince();
            this.country = fhirAddress.getCountry();
        }
    }
    private String address;
    private String city;
    private String postcode;
    private String province;
    private String country;
}
