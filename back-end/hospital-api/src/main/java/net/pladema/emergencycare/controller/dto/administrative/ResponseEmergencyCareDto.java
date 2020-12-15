package net.pladema.emergencycare.controller.dto.administrative;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseEmergencyCareDto extends ECAdministrativeDto {

    Integer id;

    //TODO add create-date attribute

    @Builder(builderMethodName = "responseAdministrativeBuilder")
    public ResponseEmergencyCareDto(Integer id){
        super();
        this.id = id;
    }
}
