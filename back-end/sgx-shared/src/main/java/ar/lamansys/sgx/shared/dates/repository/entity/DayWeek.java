package ar.lamansys.sgx.shared.dates.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "day_week")
@Getter
@Setter
@ToString
public class DayWeek implements Serializable {

    @Id
    @Column(name = "id")
    private Short id;

    @Column(name = "description", nullable = false, length = 9)
    private String description;
}
