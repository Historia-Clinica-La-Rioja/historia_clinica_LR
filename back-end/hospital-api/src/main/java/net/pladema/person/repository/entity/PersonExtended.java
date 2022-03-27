package net.pladema.person.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "person_extended")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonExtended implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3101907445998134429L;

	@Id
    @Column(name = "person_id", nullable = false)
    private Integer id;

    @Column(name = "cuil", length = 11)
    private String cuil;

    @Column(name = "mothers_last_name", length = 40)
    private String mothersLastName;

    @Column(name = "address_id")
    private Integer addressId;

	@Column(name = "phone_prefix", length = 10)
	private String phonePrefix;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "ethnicity_id")
    private Integer ethnicityId;

    @Column(name = "education_level_id")
    private Integer educationLevelId;

    @Column(name = "occupation_id")
    private Integer occupationId;

    @Column(name = "religion", length = 25)
    private String religion;

    @Column(name = "name_self_determination", length = 25)
    private String nameSelfDetermination;

    @Column(name = "gender_self_determination")
    private Short genderSelfDeterminationId;

    @Column(name = "other_gender_self_determination", length = 40)
    private String otherGenderSelfDetermination;

    @Column(name = "photo_file_path", length = 200)
    private String photoFilePath;
}
