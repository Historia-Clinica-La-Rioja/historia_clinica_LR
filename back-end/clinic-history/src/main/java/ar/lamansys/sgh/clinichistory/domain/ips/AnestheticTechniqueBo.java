package ar.lamansys.sgh.clinichistory.domain.ips;

import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EAnestheticTechnique;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EBreathing;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.ECircuit;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.ETrachealIntubation;
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

    public String getTechniqueDescription() {
        if (techniqueId == null) {
            return null;
        }

        EAnestheticTechnique technique = EAnestheticTechnique.map(techniqueId);

        if (technique == EAnestheticTechnique.BOTH)
            return EAnestheticTechnique.getIndividualOptions()
                 .stream()
                 .map(EAnestheticTechnique::getDescription)
                 .collect(Collectors.joining(" y "));

        return EAnestheticTechnique.map(techniqueId).getDescription();
    }

    public String getTrachealIntubationMethodDescription() {
        if (trachealIntubationMethodIds == null || trachealIntubationMethodIds.isEmpty()) {
            return null;
        }
        return trachealIntubationMethodIds.stream()
                .map(ETrachealIntubation::map)
                .map(ETrachealIntubation::getDescription)
                .collect(Collectors.joining(" y "));
    }

    public String getBreathingDescription() {
        if (breathingId == null) {
            return null;
        }
        return EBreathing.map(breathingId).getDescription();
    }

    public String getCircuitDescription() {
        if (circuitId == null) {
            return null;
        }
        return ECircuit.map(circuitId).getDescription();
    }

    @Override
    public void accept(IpsVisitor visitor) {
        visitor.visitAnestheticTechnique(this);
    }
}
