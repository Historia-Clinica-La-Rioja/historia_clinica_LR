package net.pladema.person.entity;

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
@Table(name = "identification_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdentificationType implements Serializable {
    /*
     */
    private static final long serialVersionUID = 6073121006647178082L;

    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "description", length = 30, nullable = false)
    private String description;
}
