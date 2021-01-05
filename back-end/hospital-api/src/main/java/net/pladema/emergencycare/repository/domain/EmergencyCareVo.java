package net.pladema.emergencycare.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmergencyCareVo implements Serializable {

	private static final long serialVersionUID = -8118445529514102823L;

	private Integer id;

	private String firstname;

	private String lastname;

	private Integer patientId;

	private Short triageCategoryId;

	private Short emergencyCareType;

	private Short emergencyCareState;

	private Integer doctorsOffice;

	private String doctorsOfficeDescription;

	private LocalDateTime createdOn;
}
