package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "procedures_status")
@Entity
public class ProceduresStatus implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3053291021636483828L;

    public static final String PLANNED = "367559008";
    public static final String ACTIVE = "385651009";
    public static final String ABANDONED = "385660001";
    public static final String SUSPENDED = "385655000";
    public static final String FAILED = "385654001";
    public static final String COMPLETE = "255594003";
    public static final String ERROR = "723510000";
    public static final String UNKNOWN = "261665006";
    public static final String DRAFT_DISCARDED = "260385009";


    @Id
    @Column(name = "id", length = 20)
    private String id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

}
