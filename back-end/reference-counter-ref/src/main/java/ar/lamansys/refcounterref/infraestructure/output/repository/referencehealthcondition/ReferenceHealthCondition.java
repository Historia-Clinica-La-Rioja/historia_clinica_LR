package ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "reference_health_condition")
@Getter
@Setter
@NoArgsConstructor
public class ReferenceHealthCondition implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7796283288603083544L;

    @EmbeddedId
    private ReferenceHealthConditionPk pk;

    public ReferenceHealthCondition(ReferenceHealthConditionPk pk) {
        this.pk = pk;
    }

}