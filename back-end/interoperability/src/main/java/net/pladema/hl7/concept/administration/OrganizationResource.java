package net.pladema.hl7.concept.administration;

import net.pladema.hl7.dataexchange.ISingleResourceFhir;
import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import net.pladema.hl7.dataexchange.model.domain.OrganizationVo;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationResource extends ISingleResourceFhir {

    @Autowired
    public OrganizationResource(FhirPersistentStore store){
        super(store);
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.Organization;
    }

    @Override
    public Organization fetch(String id, Reference[] references) {
        OrganizationVo organization = store.getOrganization(id);

        Organization resource = new Organization();
        resource.setId(organization.getId());
        resource.addIdentifier(newIdentifier(CodingSystem.REFES, organization.getId()));
        resource.setName(organization.getName());
        if(organization.hasPhoneNumber())
            resource.addTelecom(newTelecom(organization.getPhoneNumber(), ContactPoint.ContactPointUse.WORK));
        if(organization.hasAddress())
            resource.addAddress(newAddress(organization.getFullAddress()));
        resource.setActive(true);
        return resource;
    }
}
