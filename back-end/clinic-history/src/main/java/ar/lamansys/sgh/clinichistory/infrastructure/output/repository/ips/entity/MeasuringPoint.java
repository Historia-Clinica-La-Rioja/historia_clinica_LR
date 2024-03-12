package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "measuring_point")
@Entity
public class MeasuringPoint {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "blood_pressure_min")
    private Integer bloodPressureMin;

    @Column(name = "blood_pressure_max")
    private Integer bloodPressureMax;

    @Column(name = "blood_pulse")
    private Integer bloodPulse;

    @Column(name = "o2_saturation")
    private Integer o2Saturation;

    @Column(name = "co2_end_tidal")
    private Integer co2EndTidal;
}
