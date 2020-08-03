package net.pladema.medicalconsultation.diary.repository.entity;

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
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "opening_hours")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OpeningHours implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "day_week_id", nullable = false)
    private Short dayWeekId;

    @Column(name = "\"from\"",nullable = false)
    private LocalTime from;

    @Column(name = "\"to\"", nullable = false)
    private LocalTime to;

    public OpeningHours(Short dayWeekId, LocalTime from, LocalTime to){
        this.dayWeekId = dayWeekId;
        this.from = from;
        this.to = to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayWeekId,from,to);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OpeningHours other = (OpeningHours) obj;
        return Objects.equals(dayWeekId, other.dayWeekId) &&
                Objects.equals(from, other.from) &&
                Objects.equals(to, other.to);
    }

}
