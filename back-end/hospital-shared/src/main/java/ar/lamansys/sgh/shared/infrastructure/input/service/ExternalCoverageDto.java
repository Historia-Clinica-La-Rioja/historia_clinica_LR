package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;


@Setter
@Getter
@NoArgsConstructor
public class ExternalCoverageDto {

    @Nullable
    private Integer id;

    private String cuit;

    @Nullable
    private String plan;

    private String name;

    private EMedicalCoverageTypeDto type;

    public ExternalCoverageDto(@Nullable Integer id, String cuit, @Nullable String plan, String name, Short type) {
        this.id = id;
        this.cuit = cuit;
        this.plan = plan;
        this.name = name;
        this.type = EMedicalCoverageTypeDto.map(type);
    }

    public void setType(String type) {
        this.type = EMedicalCoverageTypeDto.map(type.toUpperCase());
    }
}
