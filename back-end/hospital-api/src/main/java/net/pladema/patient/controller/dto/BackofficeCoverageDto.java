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

    private String cuit;

    @Nullable
    private Integer rnos;

    @Nullable
    private String acronym;

    private Short type;

    private Boolean enabled;

    public BackofficeCoverageDto(Integer id, String name, String cuit, Short type, @Nullable Integer rnos, @Nullable String acronym, Boolean enabled) {
        this.id = id;
        this.name = name;
        this.cuit = cuit;
        this.rnos = rnos;
        this.acronym = acronym;
        this.type = type;
        this.enabled = enabled;
    }

}
