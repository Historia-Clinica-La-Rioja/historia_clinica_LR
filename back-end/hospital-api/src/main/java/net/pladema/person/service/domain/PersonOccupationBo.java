package net.pladema.person.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.patient.repository.entity.Occupation;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonOccupationBo {

    private Integer id;

    private Integer code;

    private String description;

    private boolean active;

    public PersonOccupationBo(Occupation occupation){
        this.id = occupation.getId();
        this.code = occupation.getCode();
        this.description = occupation.getDescription();
        this.active = occupation.isActive();
    }
}
