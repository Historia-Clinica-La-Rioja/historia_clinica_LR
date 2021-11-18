package ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class ReferenceHealthConditionPk implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4595832553069512453L;

    @Column(name = "reference_id", nullable = false)
    private Integer referenceId;

    @Column(name = "health_condition_id", nullable = false)
    private Integer healthConditionId;

}