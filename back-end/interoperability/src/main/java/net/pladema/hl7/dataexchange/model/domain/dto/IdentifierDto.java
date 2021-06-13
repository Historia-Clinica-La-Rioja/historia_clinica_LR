package net.pladema.hl7.dataexchange.model.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.FhirParam;
import net.pladema.hl7.dataexchange.model.adaptor.FhirString;
import org.hl7.fhir.r4.model.Identifier;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class IdentifierDto implements Serializable {

    private static final long serialVersionUID = 2269449661255773512L;

    private String system;

    private String value;

    public IdentifierDto(Identifier identifier){
        this.system = identifier.getSystem();
        this.value = identifier.getValue();
    }

    @Override
    public String toString() {
        return FhirString.joining(FhirParam.DELIMITER, system, value);
    }
}
