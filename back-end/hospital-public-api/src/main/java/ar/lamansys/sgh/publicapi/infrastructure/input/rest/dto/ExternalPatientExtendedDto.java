package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExternalPatientExtendedDto extends ExternalPatientDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    private LocalDateTime birthDate;

    private String firstName;

    private Short genderId;

    private String identificationNumber;

    private Short identificationTypeId;

    private String lastName;

    private String phoneNumber;

    private String email;

    public ExternalPatientExtendedDto(@Nullable Integer id,
                                      @Nullable Integer patientId,
                                      String externalId,
                                      String externalEncounterId,
                                      LocalDateTime externalEncounterDate,
                                      String EExternalEncounterType,
                                      LocalDateTime birthDate,
                                      String firstName,
                                      Short genderId,
                                      String identificationNumber,
                                      Short identificationTypeId,
                                      String lastName,
                                      String phoneNumber,
                                      String email) {
        super(id, patientId, externalId, externalEncounterId, externalEncounterDate, EExternalEncounterType);
        this.birthDate = birthDate;
        this.firstName = firstName;
        this.genderId = genderId;
        this.identificationNumber = identificationNumber;
        this.identificationTypeId = identificationTypeId;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
