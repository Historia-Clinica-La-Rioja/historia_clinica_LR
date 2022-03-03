package net.pladema.address.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Address implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5023340278494944407L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "street", length = 70, nullable = false)
    private String street;

    @Column(name = "number", length = 20, nullable = false)
    private String number;

    @Column(name = "floor", length = 2)
    private String floor;

    @Column(name = "apartment", length = 8)
    private String apartment;

    @Column(name = "quarter", length = 30)
    private String quarter;

    @Column(name = "city_id", nullable = false)
    private Integer cityId;

    @Column(name = "postcode", length = 6, nullable = false)
    private String postcode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

	@Column(name = "country_id")
	private Short countryId;

	@Column(name = "province_id")
	private Short provinceId;

	@Column(name = "department_id")
	private Short departmentId;

    public static Address buildDummy() {
		Address newAddress = new Address();
		newAddress.setStreet("");
		newAddress.setNumber("");
		newAddress.setCityId(1);
		newAddress.setPostcode("");
        return newAddress;
    }

}
