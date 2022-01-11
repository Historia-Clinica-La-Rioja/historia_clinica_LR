package net.pladema.establishment.repository.entity;

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
@Table(name = "private_health_insurance_plan")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PrivateHealthInsurancePlan implements Serializable {

    private static final long serialVersionUID = -1832876231321092835L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "private_health_insurance_id", nullable = false)
    private Integer privateHealthInsuranceId;

    @Column(name = "plan", nullable = false, length = 10)
    private String plan;
}
