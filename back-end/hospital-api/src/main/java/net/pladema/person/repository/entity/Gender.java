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
import java.util.List;

@Entity
@Table(name = "gender")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gender implements Serializable {
    /*
     */
    private static final long serialVersionUID = 5871312991523529690L;
    public static final short FEMALE = 1;
    public static final short MALE = 2;
	public static final short X = 3;
	public static final List<Short> GENDERS = List.of(FEMALE, MALE, X);


	@Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "description", length = 9, nullable = false)
    private String description;
}
