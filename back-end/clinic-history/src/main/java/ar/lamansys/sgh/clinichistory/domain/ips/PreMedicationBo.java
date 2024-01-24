package ar.lamansys.sgh.clinichistory.domain.ips;

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
public class PreMedicationBo extends ClinicalTerm {
    private DosageBo dosage;
    private Short viaId;

    public PreMedicationBo(String stcid, String pt, Double quantityValue, String quantityUnit, String periodUnit, LocalDateTime startDate, Short viaId) {
        this.setSnomed(new SnomedBo(stcid, pt));
        this.dosage = new DosageBo();
        this.dosage.setQuantity(new QuantityBo(quantityValue.intValue(), quantityUnit));
        this.dosage.setPeriodUnit(EUnitsOfTimeBo.map(periodUnit));
        this.dosage.setStartDate(startDate);
        this.viaId = viaId;
    }
}
