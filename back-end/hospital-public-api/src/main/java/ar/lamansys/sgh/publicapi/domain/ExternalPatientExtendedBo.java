package ar.lamansys.sgh.publicapi.domain;

import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalPatientExtendedBoException;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalPatientExtendedBoEnumException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class ExternalPatientExtendedBo extends ExternalPatientBo {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime birthDate;

    private String firstName;

    private Short genderId;

    private String identificationNumber;

    private Short identificationTypeId;

    private String lastName;

    private String phoneNumber;

    private String email;

    private List<ExternalPatientCoverageBo> medicalCoverages;

    private Integer institutionId;

    public ExternalPatientExtendedBo(@Nullable Integer patientId,
                                     @Nullable String externalId,
                                     LocalDateTime birthDate,
                                     String firstName,
                                     Short genderId,
                                     String identificationNumber,
                                     Short identificationTypeId,
                                     String lastName,
                                     String phoneNumber,
                                     String email,
                                     List<ExternalPatientCoverageBo> medicalCoverages,
                                     Integer institutionId) throws ExternalPatientExtendedBoException {
        super(patientId, externalId);
        if(birthDate==null)
            throw new ExternalPatientExtendedBoException(ExternalPatientExtendedBoEnumException.NULL_BIRTHDATE,"La fecha de nacimiento es obligatoria");
        this.birthDate = birthDate;
        if(firstName==null)
            throw new ExternalPatientExtendedBoException(ExternalPatientExtendedBoEnumException.NULL_FIRST_NAME,"El nombre es obligatorio");
        this.firstName = firstName;
        if(genderId==null)
            throw new ExternalPatientExtendedBoException(ExternalPatientExtendedBoEnumException.NULL_GENDER_ID,"El id del genero es obligatorio");
        this.genderId = genderId;
        if(identificationNumber==null)
            throw new ExternalPatientExtendedBoException(ExternalPatientExtendedBoEnumException.NULL_IDENTIFICATION_NUMBER,"El número de documento es obligatorio");
        this.identificationNumber = identificationNumber;
        if(identificationTypeId==null)
            throw new ExternalPatientExtendedBoException(ExternalPatientExtendedBoEnumException.NULL_IDENTIFICATION_TYPE_ID,"El id de tipo de documento es obligatorio");
        this.identificationTypeId = identificationTypeId;
        if(lastName==null)
            throw new ExternalPatientExtendedBoException(ExternalPatientExtendedBoEnumException.NULL_LAST_NAME,"El apellido es obligatorio");
        this.lastName = lastName;
        if(phoneNumber==null)
            throw new ExternalPatientExtendedBoException(ExternalPatientExtendedBoEnumException.NULL_PHONE_NUMBER,"El número telefónico es obligatorio");
        this.phoneNumber = phoneNumber;
        if(email==null)
            throw new ExternalPatientExtendedBoException(ExternalPatientExtendedBoEnumException.NULL_EMAIL,"El email es obligatorio");
        this.email = email;
        this.medicalCoverages = medicalCoverages;
        if(institutionId==null)
            throw new ExternalPatientExtendedBoException(ExternalPatientExtendedBoEnumException.NULL_INSTITUTION,"La institución es obligatoria");
        this.institutionId = institutionId;
    }
}