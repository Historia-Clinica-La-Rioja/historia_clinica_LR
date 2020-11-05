package net.pladema.hl7.dataexchange.mock;

import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;
import net.pladema.hl7.dataexchange.model.domain.OrganizationVo;
import org.hl7.fhir.r4.model.Address;

public class MockOrganization {

    public static OrganizationVo mock(){
        OrganizationVo resource = new OrganizationVo();
        resource.setId("10067912200333");
        resource.setName("HOSPITAL MUNICIPAL RAMON SANTAMARINA");
        resource.setPhoneNumber("");

        //Full-Address
        resource.setFullAddress(new FhirAddress(Address.AddressUse.WORK)
                .setAddress("GRAL. PAZ","1400","","")
                .setCity("TANDIL")
                .setPostcode("7000")
                .setCountry("ARGENTINA"));
        return resource;
    }
}
