package net.pladema.patient.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.service.domain.PrivateHealthInsuranceDetailsBo;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "private_Health_insurance_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateHealthInsuranceDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public PrivateHealthInsuranceDetails(LocalDate startDate, LocalDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public PrivateHealthInsuranceDetails(PrivateHealthInsuranceDetailsBo privateHealthInsuranceDetailsBo){
        if (privateHealthInsuranceDetailsBo != null) {
            this.startDate = privateHealthInsuranceDetailsBo.getStartDate();
            this.endDate = privateHealthInsuranceDetailsBo.getEndDate();
        }
    }
}
