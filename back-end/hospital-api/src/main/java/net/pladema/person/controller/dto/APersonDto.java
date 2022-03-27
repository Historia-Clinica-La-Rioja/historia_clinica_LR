package net.pladema.person.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class APersonDto {

    @NotNull
    @Length(max = 40, message = "{person.name.max.value}")
    private String firstName;

    @Length(max = 40, message = "{person.name.max.value}")
    private String middleNames;

    @NotNull
    @Length(max = 40, message = "{person.name.max.value}")
    private String lastName;

    @Length(max = 40, message = "{person.name.max.value}")
    private String otherLastNames;

    @NotNull
    private Short identificationTypeId;

    private String identificationNumber;

    @NotNull
    private Short genderId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime birthDate;

    private String cuil;

    @Length(max = 40, message = "{person.name.max.value}")
    private String mothersLastName;

	private String phonePrefix;

    private String phoneNumber;

    private String email;

    private Integer ethnicityId;

    private Integer educationLevelId;

    private Integer occupationId;

    private String religion;

    private String nameSelfDetermination;

    private Short genderSelfDeterminationId;

    @Length(max = 40)
    @Nullable
    private String otherGenderSelfDetermination;

    private String street;

    private String number;

    private String floor;

    private String apartment;

    private String quarter;

    private Integer cityId;

    private String postcode;

	private Short countryId;

	private Short provinceId;

	private Short departmentId;

}
