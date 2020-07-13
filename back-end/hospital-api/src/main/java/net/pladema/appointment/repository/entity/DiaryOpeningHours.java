package net.pladema.appointment.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "diary_opening_hours")
@Getter
@Setter
@ToString
public class DiaryOpeningHours implements Serializable {

    @EmbeddedId
    private DiaryOpeningHoursPK pk;

    @Column(name = "medical_attention_type_id", nullable = false)
    private Short medicalAttentionTypeId;

    @Column(name = "overturn_count", columnDefinition = "smallint default 0", nullable = false)
    private Short overturnCount;
}
