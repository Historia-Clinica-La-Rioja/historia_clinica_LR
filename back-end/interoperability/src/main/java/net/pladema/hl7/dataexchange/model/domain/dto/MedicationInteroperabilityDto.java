package net.pladema.hl7.dataexchange.model.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pladema.hl7.dataexchange.model.adaptor.FhirDateMapper;
import net.pladema.hl7.dataexchange.model.domain.DosageVo;
import net.pladema.hl7.dataexchange.model.domain.MedicationVo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class MedicationInteroperabilityDto {
    public MedicationInteroperabilityDto(MedicationVo medicationVo){
        this.id = medicationVo.getId();
        this.sctidCode = medicationVo.getSctidCode();
        this.sctidTerm = medicationVo.getSctidTerm();
        this.formCode = medicationVo.getFormCode();
        this.formTerm = medicationVo.getFormTerm();

        medicationVo.getIngredients().forEach(ingredient ->
            this.ingredients.add(new MedicationIngredientDto(ingredient))
        );

        this.statementId = medicationVo.getStatementId();

        DosageVo dosage = medicationVo.getDosage();
        if(dosage != null) {
            this.routeCode = dosage.getRouteCode();
            this.routeTerm = dosage.getRouteTerm();
            this.unitTime = dosage.getPeriodUnit();
            this.doseQuantityCode = dosage.getDoseQuantityCode();
            this.doseQuantityValue = BigDecimal.valueOf(dosage.getDoseQuantityValue());
            this.doseQuantityUnit = dosage.getDoseQuantityUnit();
        }
        this.status = medicationVo.getStatus();
        this.effectiveTimeStart = FhirDateMapper.toLocalDate(medicationVo.getEffectiveTimeStart());
        this.effectiveTimeEnd = FhirDateMapper.toLocalDate(medicationVo.getEffectiveTimeEnd());

    }
    private String id;

    //Medication info
    private String sctidCode;
    private String sctidTerm;

    //presentation of the medicinal product
    private String formCode;
    private String formTerm;

    private final List<MedicationIngredientDto> ingredients = new ArrayList<>();

    //=====================Statement=====================
    private String statementId;

    //route administration
    private String routeCode;
    private String routeTerm;

    private String status;

    //Dosage timing unit time
    private String unitTime;

    //Dose Quantity
    private BigDecimal doseQuantityValue;
    private String doseQuantityCode;
    private String doseQuantityUnit;

    private LocalDate effectiveTimeStart;
    private LocalDate effectiveTimeEnd;
}
