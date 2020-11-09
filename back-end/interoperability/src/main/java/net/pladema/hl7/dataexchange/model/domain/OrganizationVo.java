package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.Cast;
import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;

@Getter
@Setter
@NoArgsConstructor
public class OrganizationVo {

    public OrganizationVo(Object[] tuple) {
        int index=0;
        setId(Cast.toString(tuple[index++]));
        setName(Cast.toString(tuple[index++]));
        setPhoneNumber(Cast.toString(tuple[index]));
    }

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
