package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BedBo implements Serializable {

	private static final long serialVersionUID = -2293306512355827552L;

	private Integer id;

    private String bedNumber;

    private RoomBo room;
}
