package net.pladema.person.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.patient.repository.entity.EducationLevel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EducationLevelBo {

    private Integer id;

    private Integer code;

    private String description;

    private boolean active;

    public EducationLevelBo(EducationLevel educationLevel){
        this.id = educationLevel.getId();
        this.code = educationLevel.getCode();
        this.description = educationLevel.getDescription();
        this.active = educationLevel.isActive();
    }
}
