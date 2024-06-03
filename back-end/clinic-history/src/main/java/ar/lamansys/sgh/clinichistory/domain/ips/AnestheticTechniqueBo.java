package ar.lamansys.sgh.clinichistory.domain.ips;

import java.util.List;

import ar.lamansys.sgh.clinichistory.domain.ips.visitor.IpsVisitor;
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
public class AnestheticTechniqueBo extends ClinicalTerm implements IpsBo {

    private Short techniqueId;
    private Boolean trachealIntubation;
    private List<Short> trachealIntubationMethodIds;
    private Short breathingId;
    private Short circuitId;

    public AnestheticTechniqueBo(Integer anestheticTechniqueId, String stcid, String pt, Short techniqueId, Boolean trachealIntubation,
                                 Short breathingId, Short circuitId) {
        this.setId(anestheticTechniqueId);
        this.setSnomed(new SnomedBo(stcid, pt));
        this.techniqueId = techniqueId;
        this.trachealIntubation = trachealIntubation;
        this.breathingId = breathingId;
        this.circuitId = circuitId;
    }

    @Override
    public void accept(IpsVisitor visitor) {
        visitor.visitAnestheticTechnique(this);
    }
}
