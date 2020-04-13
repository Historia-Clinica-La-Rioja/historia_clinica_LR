package net.pladema.person.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

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
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(name = "rnos", length = 6, nullable = false)
    private String rnos;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "acronym", length = 18)
    private String acronym;

    @Column(name = "query_date", nullable = false)
    private LocalDate queryDate;
}
