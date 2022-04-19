package ar.lamansys.online.infraestructure.output.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "v_booking_health_insurance")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VBookingHealthInsurance {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "description")
    private String description;

}
