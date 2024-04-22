package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "condition_clinical_status")
@Entity
public class ConditionClinicalStatus implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3053291021636483828L;

    public static final String ACTIVE = "55561003";
    public static final String INACTIVE = "73425007";
    public static final String REMISSION = "277022003";
    public static final String SOLVED = "723506003";
    public static final String DRAFT_DISCARDED = "260385009";

    @Id
    @Column(name = "id", length = 20)
    private String id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    public static String[] downState() {
        return new String[]{REMISSION, SOLVED};
    }
}
