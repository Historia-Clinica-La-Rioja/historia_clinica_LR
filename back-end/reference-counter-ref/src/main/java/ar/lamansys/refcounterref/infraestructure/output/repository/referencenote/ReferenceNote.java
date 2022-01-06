package ar.lamansys.refcounterref.infraestructure.output.repository.referencenote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "reference_note")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceNote implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5588042884999360537L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "description")
    private String description;

    public ReferenceNote(String description) {
        this.description = description;
    }

}
