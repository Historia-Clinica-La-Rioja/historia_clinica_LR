package net.pladema.person.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.person.repository.entity.Ethnicity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EthnicityBo {

    private Integer id;

    private String sctid;

    private String pt;

    private boolean active;

    public EthnicityBo(Ethnicity ethnicity) {
        this.id = ethnicity.getId();
        this.sctid = ethnicity.getSctid();
        this.pt = ethnicity.getPt();
        this.active = ethnicity.isActive();
    }

}
