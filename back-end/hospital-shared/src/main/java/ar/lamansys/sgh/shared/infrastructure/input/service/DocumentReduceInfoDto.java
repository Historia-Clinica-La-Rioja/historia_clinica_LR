package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DocumentReduceInfoDto implements Serializable {

	private static final long serialVersionUID = 1630523222071677056L;

	private Integer sourceId;

	private Short typeId;

	private Integer createdBy;

	private LocalDateTime createdOn;

}
