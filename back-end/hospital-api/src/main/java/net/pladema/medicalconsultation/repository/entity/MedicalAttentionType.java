package net.pladema.medicalconsultation.repository.entity;

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


    public static final Short PROGRAMMED = 1;
    public static final Short SPONTANEOUS = 2;

    @Id
    @Column(name = "id")
    private Short id;

    @Column(name = "description", nullable = false, length = 10)
    private String description;
}
