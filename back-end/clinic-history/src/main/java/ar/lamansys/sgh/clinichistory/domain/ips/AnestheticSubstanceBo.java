package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EAnestheticSubstanceType;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EUnitsOfTimeBo;
import java.time.LocalDateTime;
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
    private DosageBo dosage;
    private Short viaId;
    private String viaNote;
    private Short typeId;

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
}
