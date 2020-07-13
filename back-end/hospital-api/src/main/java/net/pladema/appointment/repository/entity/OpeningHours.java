package net.pladema.appointment.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Table(name = "opening_hours")
@Getter
@Setter
@ToString
public class OpeningHours implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "day_week_id", nullable = false)
    private Short dayWeekId;

    @Column(name = "from", nullable = false)
    private LocalTime from;

    @Column(name = "to", nullable = false)
    private LocalTime to;

}
