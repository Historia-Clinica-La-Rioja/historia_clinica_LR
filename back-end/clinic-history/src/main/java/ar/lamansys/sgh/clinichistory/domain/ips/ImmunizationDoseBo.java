package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class ImmunizationDoseBo {

    @ToString.Include
    @EqualsAndHashCode.Include
    private String description;

    @ToString.Include
    @EqualsAndHashCode.Include
    private Short order;

    public ImmunizationDoseBo(String description, Short order) {
        this.description = description;
        this.order = order;
    }
}
