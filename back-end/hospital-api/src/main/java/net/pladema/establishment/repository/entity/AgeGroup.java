package net.pladema.establishment.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "age_group")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class AgeGroup {
    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Short id;

    @Column(name = "description", nullable = false)
    private String description;
}
