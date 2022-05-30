package net.pladema.reports.service.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.reports.repository.ConsultationsVo;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class ConsultationsBo {

    private Integer id;

    private Long documentId;

    private LocalDate consultationDate;

    private String specialty;

    private String completeProfessionalName;

	private String completeProfessionalNameSelfDetermination;

    public ConsultationsBo(ConsultationsVo consultationsVo){
        this.id = consultationsVo.getId();
        this.documentId = consultationsVo.getDocumentId();
        this.consultationDate = consultationsVo.getConsultationDate();
        this.specialty = consultationsVo.getSpecialty();
        this.completeProfessionalName = Stream.of(consultationsVo.getFirstName(), consultationsVo.getMiddleNames(), consultationsVo.getLastName(), consultationsVo.getOtherLastNames())
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));
		this.completeProfessionalNameSelfDetermination = getNameSelfDetermination(consultationsVo);
	}

	private String getNameSelfDetermination(ConsultationsVo consultationsVo){
		if(consultationsVo.getNameSelfDetermination() == null || consultationsVo.getNameSelfDetermination().isEmpty()) {
			return null;
		}
		return Stream.of(consultationsVo.getNameSelfDetermination(), consultationsVo.getLastName(), consultationsVo.getOtherLastNames())
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));
	}

}
