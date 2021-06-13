package net.pladema.person.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.person.service.domain.EthnicityBo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ethnicity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ethnicity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sctid", length = 20, nullable = false)
    private String sctid;

    @Column(name = "pt", length = 255, nullable = false)
    private String pt;

    @Column(name = "active", nullable = false)
    private boolean active;

    public Ethnicity(EthnicityBo ethnicityBo) {
        this.id = ethnicityBo.getId();
        this.sctid = ethnicityBo.getSctid();
        this.pt = ethnicityBo.getPt();
        this.active = ethnicityBo.isActive();
    }

}
