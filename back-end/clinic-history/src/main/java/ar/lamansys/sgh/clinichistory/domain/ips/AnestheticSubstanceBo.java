package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EAnestheticSubstanceType;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EVia;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AnestheticSubstanceBo extends ClinicalTerm {

    private static final Map<Short, List<EVia>> VIA_MAP = new HashMap<>();

    private DosageBo dosage;
    private Short viaId;
    private String viaNote;
    private Short typeId;

    static {
        VIA_MAP.put(EAnestheticSubstanceType.PRE_MEDICATION.getId(), EVia.getPreMedication());
        VIA_MAP.put(EAnestheticSubstanceType.ANESTHETIC_PLAN.getId(), EVia.getAnestheticPlan());
        VIA_MAP.put(EAnestheticSubstanceType.ANESTHETIC_AGENT.getId(), EVia.getAnestheticAgent());
        VIA_MAP.put(EAnestheticSubstanceType.NON_ANESTHETIC_DRUG.getId(), EVia.getNonAnestheticDrug());
        VIA_MAP.put(EAnestheticSubstanceType.ANTIBIOTIC_PROPHYLAXIS.getId(), EVia.getAntibioticProphylaxis());
    }

    public AnestheticSubstanceBo(String stcid, String pt, Double quantityValue, String quantityUnit, String periodUnit, LocalDateTime startDate, Short viaId, String viaNote, Short typeId) {
        this.setSnomed(new SnomedBo(stcid, pt));
        this.dosage = new DosageBo();
        this.dosage.setQuantity(new QuantityBo(quantityValue.intValue(), quantityUnit));
        this.dosage.setPeriodUnit(EUnitsOfTimeBo.map(periodUnit));
        this.dosage.setStartDate(startDate);
        this.viaId = viaId;
        this.viaNote = viaNote;
        this.typeId = typeId;
    }

    public boolean isOfType(EAnestheticSubstanceType substanceType) {
        return typeId != null && substanceType.equals(EAnestheticSubstanceType.map(typeId));
    }

    public List<EVia> getStaticDefinedVias() {
        return typeId != null ? VIA_MAP.get(typeId) : List.of();
    }

    public String getDescriptionType() {
        return typeId != null ? EAnestheticSubstanceType.map(typeId).getDescription() : null;
    }
}
