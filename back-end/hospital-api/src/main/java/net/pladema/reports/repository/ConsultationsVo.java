package net.pladema.reports.repository;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConsultationsVo {

    private Integer id;

    private Long documentId;

    private LocalDateTime consultationDate;

    private String specialty;

    private String firstName;

    private String middleNames;

    private String lastName;

    private String otherLastNames;

	private String nameSelfDetermination;
}
