package ar.lamansys.sgh.publicapi.domain;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;


@Setter
@Getter
public class ExternalCoverageBo {

    @Nullable
    private Integer id;

    private String cuit;

    @Nullable
    private String plan;

    private String name;

    private EMedicalCoverageType type;

    public ExternalCoverageBo(@Nullable Integer id, String cuit, @Nullable String plan, String name, String type){
        this.id = id;
        this.cuit = cuit;
        this.plan = plan;
        this.name = name;
        this.type = EMedicalCoverageType.map(type.toUpperCase());
    }
    public void setType(String type) {
        this.type =  EMedicalCoverageType.map(type.toUpperCase());
    }
}
