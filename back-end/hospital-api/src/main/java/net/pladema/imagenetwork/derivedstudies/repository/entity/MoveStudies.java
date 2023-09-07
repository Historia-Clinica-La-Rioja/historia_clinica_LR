package net.pladema.imagenetwork.derivedstudies.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "move_studies")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MoveStudies implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "appointment_id")
	private Integer appointmentId;

	@Column(name = "orchestrator_id")
	private Integer orchestratorId;

	@Column(name = "image_id")
	private String imageId;

	@Column(name = "size_image")
	private Integer sizeImage;

	@Column(name = "pac_server_id")
	private Integer pacServerId;

	@Column(name = "priority")
	private Integer priority;

	@Column(name = "result")
	private String result;

	@Column(name = "move_date")
	private Date moveDate;

	@Column(name = "priority_max")
	private Integer priorityMax;

	@Column(name = "attemps_number")
	private Integer attempsNumber;

	@Column(name = "status")
	private String status;

	@Column(name = "institution_id", nullable = true)
	private Integer institutionId;

	@Column(name = "begin_of_move")
	private Date beginOfMove;
	@Column(name = "end_of_move")
	private Date endOfMove;
	public MoveStudies(Integer appointmentId, Integer orchestratorId, String imageId, Integer pacServerId, Integer priority, Date moveDate, Integer priorityMax, Integer attempsNumber, String status, Integer institutionId){
		this.appointmentId = appointmentId;
		this.orchestratorId = orchestratorId;
		this.imageId = imageId;
		this.pacServerId = pacServerId;
		this.priority = priority;
		this.moveDate = moveDate;
		this.priorityMax = priorityMax;
		this.attempsNumber = attempsNumber;
		this.status = status;
		this.institutionId =institutionId;
		this.result ="";
	}

}
