package net.pladema.patient.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.person.service.domain.PersonOccupationBo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "occupation")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Occupation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", nullable = false)
    private Integer code;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active;

    public Occupation(PersonOccupationBo personOccupationBo){
        this.id = personOccupationBo.getId();
        this.code = personOccupationBo.getCode();
        this.description = personOccupationBo.getDescription();
        this.active = personOccupationBo.isActive();
    }
}
