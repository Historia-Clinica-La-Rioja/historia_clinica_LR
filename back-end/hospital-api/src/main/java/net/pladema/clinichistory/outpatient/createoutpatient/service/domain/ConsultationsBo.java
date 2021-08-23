package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.outpatient.repository.domain.ConsultationsVo;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class ConsultationsBo {

    private Integer id;

    private LocalDate consultationDate;

    private String specialty;

    private String completeProfessionalName;

    public ConsultationsBo(ConsultationsVo consultationsVo){
        this.id = consultationsVo.getId();
        this.consultationDate = consultationsVo.getConsultationDate();
        this.specialty = consultationsVo.getSpecialty();
        this.completeProfessionalName = Stream.of(consultationsVo.getFirstName(), consultationsVo.getMiddleNames(), consultationsVo.getLastName(), consultationsVo.getOtherLastNames())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }

}
