package net.pladema.clinichistory.outpatient.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "source_type")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SourceType implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -596696291256492911L;

    public static final short INTERNACION = (short)0;
    public static final short AMBULATORIA = (short)1;

    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "description", nullable = false, length = 20)
    private String description;

}