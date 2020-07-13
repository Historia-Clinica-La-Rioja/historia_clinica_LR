package net.pladema.appointment.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "medical_attention_type")
@Getter
@Setter
@ToString
public class MedicalAttentionType implements Serializable {

    @Id
    @Column(name = "id")
    private Short id;

    @Column(name = "description", nullable = false, length = 10)
    private String description;
}
