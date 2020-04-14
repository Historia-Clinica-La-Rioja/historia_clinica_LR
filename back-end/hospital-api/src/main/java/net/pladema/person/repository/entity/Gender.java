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
@Table(name = "gender")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gender implements Serializable {
    /*
     */
    private static final long serialVersionUID = 5871312991523529690L;

    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "description", length = 9, nullable = false)
    private String description;
}
