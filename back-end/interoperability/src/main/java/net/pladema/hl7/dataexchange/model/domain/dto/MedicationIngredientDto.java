package net.pladema.hl7.dataexchange.model.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pladema.hl7.dataexchange.model.domain.MedicationIngredientVo;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class MedicationIngredientDto {

    public MedicationIngredientDto(MedicationIngredientVo medicationIngredientVo){
        this.sctidCode = medicationIngredientVo.getSctidCode();
        this.sctidTerm = medicationIngredientVo.getSctidTerm();
        this.active = medicationIngredientVo.isActive();
        this.unitMeasure = medicationIngredientVo.getUnitMeasure();
        this.unitValue = medicationIngredientVo.getUnitValue();
        this.presentationUnit = medicationIngredientVo.getPresentationUnit();
        this.presentationValue = medicationIngredientVo.getPresentationValue();
    }
    private String sctidCode;

    private String sctidTerm;

    private boolean active;

    private String unitMeasure;
    private BigDecimal unitValue;

    private String presentationUnit;
    private BigDecimal presentationValue;
}
