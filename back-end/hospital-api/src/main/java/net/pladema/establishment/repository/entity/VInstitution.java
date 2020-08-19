package net.pladema.establishment.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "v_institution")
@Getter
@Setter
@ToString
public class VInstitution {

    @EmbeddedId
    private VInstitutionPK pk;

    @Column(name = "latitud")
    private Double latitude;

    @Column(name = "longitud")
    private Double longitude;

    @Column(name = "covid_presumptive")
    private Boolean covidPresumtive;

    public boolean isCovidPresumtive(){
        return covidPresumtive != null && covidPresumtive;
    }

}
