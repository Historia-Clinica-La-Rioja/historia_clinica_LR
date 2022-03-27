package net.pladema.snowstorm.repository.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SnomedRelatedGroupPK implements Serializable {

    @Column(name = "snomed_id", nullable = false)
    private Integer snomedId;

    @Column(name = "group_id", nullable = false)
    private Integer groupId;

}
