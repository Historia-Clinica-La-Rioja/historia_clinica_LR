package net.pladema.person.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.address.repository.entity.Address;
import net.pladema.address.repository.entity.City;
import net.pladema.address.repository.entity.Province;
import net.pladema.person.repository.entity.HealthInsurance;
import net.pladema.person.repository.entity.IdentificationType;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PersonalInformation {

    private Integer id;

    private String identificationNumber;

    private LocalDate birthDate;

    private String email;

    private String cuil;

    private String phoneNumber;

    private IdentificationType identificationType;

    private HealthInsurance healthInsurance;

    private Address address;

    private City city;

    private Province province;

    public PersonalInformation(Integer personId, String identificationNumber, LocalDate birthDate, String email,
                               Short identificationTypeId, String identificationTypeDesc,
                               String cuil, String phoneNumber,
                               Short healthInsuranceId, String acronym, String rnos,
                               Integer addressId, String street, String number, String floor, String apartment,
                               Integer cityId, String city, Short provinceId, String province){
        this.id = personId;
        this.identificationNumber = identificationNumber;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.cuil = cuil;

        this.identificationType = new IdentificationType(identificationTypeId, identificationTypeDesc);

        this.healthInsurance =  new HealthInsurance();
        this.healthInsurance.setId(healthInsuranceId);
        this.healthInsurance.setAcronym(acronym);
        this.healthInsurance.setRnos(rnos);

        this.address = new Address();
        this.address.setId(addressId);
        this.address.setStreet(street);
        this.address.setNumber(number);
        this.address.setFloor(floor);
        this.address.setApartment(apartment);

        this.city = new City();
        this.city.setId(cityId);
        this.city.setDescription(city);

        this.province = new Province();
        this.province.setId(provinceId);
        this.province.setDescription(province);
    }
}
