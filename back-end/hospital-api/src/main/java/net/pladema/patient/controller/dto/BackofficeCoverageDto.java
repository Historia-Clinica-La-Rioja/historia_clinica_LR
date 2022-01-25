package net.pladema.patient.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class BackofficeCoverageDto implements Serializable {

    private Integer id;

    private String name;

    @Nullable
    private Integer rnos;

    @Nullable
    private String acronym;

    @Nullable
    private String plan;

    private Short type;

    public BackofficeCoverageDto(Integer id, String name, Short type, @Nullable Integer rnos, @Nullable String acronym, @Nullable String plan) {
        this.id = id;
        this.name = name;
        this.rnos = rnos;
        this.acronym = acronym;
        this.plan = plan;
        this.type = type;
    }

}
