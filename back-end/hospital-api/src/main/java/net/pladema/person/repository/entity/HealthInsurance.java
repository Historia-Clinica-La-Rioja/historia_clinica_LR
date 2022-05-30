package net.pladema.person.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.repository.entity.MedicalCoverage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "health_insurance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthInsurance extends MedicalCoverage {
    /*
     */
    private static final long serialVersionUID = 2873716268832417941L;

    @Column(name = "rnos", nullable = false)
    private Integer rnos;

    @Column(name = "acronym", length = 18)
    private String acronym;

    public HealthInsurance(Integer id, String name, String cuit, Integer rnos, String acronym, Short type){
        setId(id);
        setName(name);
        setCuit(cuit);
		setType(type);
        this.rnos = rnos;
        this.acronym = acronym;
    }

    public HealthInsurance(Integer id,
                           String name,
                           Integer createBy,
                           Timestamp createOn,
                           Integer updateBy,
                           Timestamp updateOn,
                           Boolean deleted,
                           Integer deletedBy,
                           Timestamp deletedOn,
                           String cuit,
						   Short type){
        setId(id);
        setName(name);
        setCuit(cuit);
		setType(type);
        setCreatedBy(createBy);
        setCreatedOn(createOn.toLocalDateTime());
        setUpdatedBy(updateBy);
        setUpdatedOn(updateOn.toLocalDateTime());
        setDeleted(deleted);
        if(deletedOn!=null) {
            setDeletedBy(deletedBy);
            setDeletedOn(deletedOn.toLocalDateTime());
        }
    }
}
