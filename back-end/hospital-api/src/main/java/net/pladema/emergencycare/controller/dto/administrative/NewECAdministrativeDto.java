package net.pladema.emergencycare.controller.dto.administrative;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@Getter
@ToString
public class NewECAdministrativeDto extends ECAdministrativeDto {

    //TODO add patient attribute

    @Builder(builderMethodName = "newAdministrativeBuilder")
    public NewECAdministrativeDto(){
        super();
    }
}
