package net.pladema.patient.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "identity_verification_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IdentityVerificationStatus implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4984026321724492182L;

    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "description", length = 20, nullable = false)
    private String description;

}
