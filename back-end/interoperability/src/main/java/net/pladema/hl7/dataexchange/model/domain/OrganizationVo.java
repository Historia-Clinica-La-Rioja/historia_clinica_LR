package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;
import net.pladema.hl7.dataexchange.model.adaptor.FhirString;

@Getter
@Setter
@NoArgsConstructor
public class OrganizationVo {

    public OrganizationVo(String sisaCode, String name, String phoneNumber, Integer addressId) {
        setId(sisaCode);
        setName(name);
        setPhoneNumber(phoneNumber);
        setAddressId(addressId);
    }

    private String id;

    private String name;

    private String phoneNumber;

    private Integer addressId;
    private FhirAddress fullAddress;

    public boolean hasPhoneNumber() {
        return FhirString.hasText(phoneNumber);
    }

    public boolean hasAddress() {
        return fullAddress != null;
    }
}
