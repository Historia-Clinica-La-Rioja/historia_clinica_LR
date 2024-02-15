package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "personal_history_type")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PersonalHistoryType {

    public static final String HABIT = "1";
    public static final String CLINICAL = "2";
    public static final String SURGICAL = "3";

    @Id
    @Column(name = "id")
    private Short id;

    @Column(name = "description", length = 20, nullable = false)
    private String description;
}
