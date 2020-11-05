package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;

@Getter
@Setter
@NoArgsConstructor
public class OrganizationVo {

    private String id;

    private String name;

    private String phoneNumber;

    private FhirAddress fullAddress;

    public boolean hasPhoneNumber() {
        return phoneNumber != null && !phoneNumber.isBlank();
    }

    public boolean hasAddress() {
        return fullAddress != null && fullAddress.hasAddressData();
    }
}
