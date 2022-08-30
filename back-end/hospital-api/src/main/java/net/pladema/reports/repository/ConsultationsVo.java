package net.pladema.reports.repository;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConsultationsVo {

    private Integer id;

    private Long documentId;

    private LocalDate consultationDate;

    private String specialty;

    private String firstName;

    private String middleNames;

    private String lastName;

    private String otherLastNames;

	private String nameSelfDetermination;
}
