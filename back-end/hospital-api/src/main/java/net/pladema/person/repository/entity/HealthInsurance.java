package net.pladema.person.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "health_insurance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthInsurance implements Serializable {
    /*
     */
    private static final long serialVersionUID = 2873716268832417941L;

    @Id
    @Column(name = "rnos", nullable = false)
    private Integer rnos;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "acronym", length = 18)
    private String acronym;
}
