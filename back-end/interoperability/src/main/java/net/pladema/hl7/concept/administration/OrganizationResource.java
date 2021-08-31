package net.pladema.hl7.concept.administration;

import net.pladema.hl7.dataexchange.ISingleResourceFhir;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import net.pladema.hl7.dataexchange.model.domain.OrganizationVo;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Conditional(InteroperabilityCondition.class)
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
    public Organization fetch(String id, Map<ResourceType, Reference> references) {
        OrganizationVo organization = store.getOrganization(id);

        Organization resource = new Organization();
        resource.setId(getSisaCode());
        resource.addIdentifier(newIdentifier(CodingSystem.REFES, getSisaCode()));
        resource.setName(organization.getName());
        if(organization.hasPhoneNumber())
            resource.addTelecom(newTelecom(organization.getPhoneNumber(), ContactPoint.ContactPointUse.WORK));
        if(organization.hasAddress())
            resource.addAddress(newAddress(organization.getFullAddress()));
        resource.setActive(true);
        return resource;
    }

    public static OrganizationVo encode(IBaseResource baseResource) {
        OrganizationVo data = new OrganizationVo();
        Organization resource = (Organization) baseResource;
        data.setId(resource.getId());
        data.setName(resource.getName());
        if(resource.hasTelecom())
            data.setPhoneNumber(resource.getTelecom().get(0).getValue());
        if(resource.hasAddress())
            data.setFullAddress(decodeAddress(resource.getAddress().get(0)));
        return data;
    }
}
