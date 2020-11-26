package net.pladema.hl7.dataexchange.model.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDto implements Serializable {

    private String id;
    private String name;
    private IdentifierDto identifier;

    public String getCustodian(){
        return identifier != null ? identifier.getValue() : null;
    }
}
