package ar.lamansys.sgh.clinichistory.domain.ips;

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
public class AnalgesicTechniqueBo extends AnestheticSubstanceBo {

    private Integer analgesicTechniqueId;
    private String injectionNote;
    private Boolean catheter;
    private String catheterNote;
    public AnalgesicTechniqueBo(String stcid, String pt, Double quantityValue, String quantityUnit, String periodUnit,
                                Integer analgesicTechniqueId, String injectionNote, Boolean catheter, String catheterNote) {
        super(stcid, pt, quantityValue, quantityUnit, periodUnit, null, null, null, null);
        this.analgesicTechniqueId = analgesicTechniqueId;
        this.injectionNote = injectionNote;
        this.catheter = catheter;
        this.catheterNote = catheterNote;
    }
}
