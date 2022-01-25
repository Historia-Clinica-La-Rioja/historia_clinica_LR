package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;

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

    private List<ExternalPatientCoverageDto> medicalCoverages;

    private Integer institutionId;

    public ExternalPatientExtendedDto(@Nullable Integer patientId,
                                      String externalId,
                                      LocalDateTime birthDate,
                                      String firstName,
                                      Short genderId,
                                      String identificationNumber,
                                      Short identificationTypeId,
                                      String lastName,
                                      String phoneNumber,
                                      String email,
                                      List<ExternalPatientCoverageDto> medicalCoverages,
                                      Integer institutionId) {
        super(patientId, externalId);
        this.birthDate = birthDate;
        this.firstName = firstName;
        this.genderId = genderId;
        this.identificationNumber = identificationNumber;
        this.identificationTypeId = identificationTypeId;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.medicalCoverages = medicalCoverages;
        this.institutionId = institutionId;
    }
}
