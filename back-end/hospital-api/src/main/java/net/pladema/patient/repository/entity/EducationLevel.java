package net.pladema.patient.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.person.service.domain.EducationLevelBo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "education_level")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EducationLevel{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", nullable = false)
    private Integer code;

    @Column(name = "description", length = 50, nullable = false)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active;

    public EducationLevel(EducationLevelBo educationLevelBo){
        this.id = educationLevelBo.getId();
        this.code = educationLevelBo.getCode();
        this.description = educationLevelBo.getDescription();
        this.active = educationLevelBo.isActive();
    }
}
