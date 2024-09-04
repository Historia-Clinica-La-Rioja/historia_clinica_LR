package net.pladema.imagenetwork.derivedstudies.service.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MoveStudiesBO {

	private Integer id;

	private Integer appointmentId;

	private Integer orchestratorId;

	private String imageId;

	private Integer sizeImage;

	private Integer pacServerId;

	private Integer priority;

	private String result;

	private Date derivedDate;

	private Integer priorityMax;

	private Integer attempsNumber;

	private String status;

	private Double calculatedPriority;

	private Integer institutionId;

	private String domainPac;

	private Date beginOfMove;

	private Date endOfMove;
}
