package ar.lamansys.sgx.shared.dates.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "date_type")
@Getter
@Setter
@ToString
public class DateType implements Serializable {

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "day", length = 2, nullable = false)
    private String day;

    @Column(name = "month", length = 2, nullable = false)
    private String month;

    @Column(name = "year", nullable = false)
    private Short year;

    @Column(name = "day_week_id", nullable = false)
    private Short dayWeekId;
}
