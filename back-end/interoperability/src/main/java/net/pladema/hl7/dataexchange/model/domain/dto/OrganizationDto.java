package net.pladema.hl7.dataexchange.model.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.domain.OrganizationVo;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class OrganizationDto implements Serializable {

    private static final long serialVersionUID = -547359865504200892L;

    private String id;
    private String name;
    private IdentifierDto identifier;
    private String phoneNumber;
    private transient FhirAddressDto address;

    public OrganizationDto(String id, String name, IdentifierDto identifier){
        this.id = id;
        this.name = name;
        this.identifier = identifier;
    }

    public OrganizationDto(OrganizationVo organization) {
        if(organization != null) {
            this.setName(organization.getName());
            this.setId(organization.getId());
            this.setPhoneNumber(organization.getPhoneNumber());
            this.setAddress(new FhirAddressDto(organization.getFullAddress()));
        }
    }

    public String getCustodian(){
        return identifier != null ? identifier.getValue() : null;
    }
}
