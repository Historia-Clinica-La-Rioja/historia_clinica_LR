package net.pladema.patient.controller.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.service.domain.MedicalCoverageBo;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value= HealthInsuranceDto.class, name="2"),
        @JsonSubTypes.Type(value= PrivateHealthInsuranceDto.class, name="1")
})
public abstract class CoverageDto implements Serializable {

    private Integer id;

    private String name;

    private String cuit;

	private Short type;

    public abstract MedicalCoverageBo newInstance();
}
