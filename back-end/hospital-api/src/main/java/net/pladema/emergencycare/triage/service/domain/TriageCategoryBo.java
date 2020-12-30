package net.pladema.emergencycare.triage.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.triage.repository.entity.TriageCategory;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TriageCategoryBo {

    private Short id;

    private String name;

    private String description;

    private String colorName;

    private String colorCode;

    public TriageCategoryBo(TriageCategory triageCategory){
        this.id = triageCategory.getId();
        this.name = triageCategory.getName();
        this.description = triageCategory.getDescription();
        this.colorName = triageCategory.getColorName();
        this.colorCode = triageCategory.getColorCode();
    }

}
