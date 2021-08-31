package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "problem_type")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProblemType implements Serializable {

    public static final String DIAGNOSIS = "439401001";
    public static final String PROBLEM = "55607006";
    public static final String HISTORY = "57177007";
    public static final String CHRONIC = "-55607006";


    @Id
    @Column(name = "id", length = 20)
    private String id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;
}
