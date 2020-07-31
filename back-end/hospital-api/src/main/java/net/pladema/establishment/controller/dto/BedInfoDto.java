package net.pladema.establishment.controller.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.patient.controller.dto.BasicPatientDto;

@Getter
@Setter
@ToString
public class BedInfoDto implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1999979362232232018L;

	private BedDto bed;

    private BasicPatientDto patient;
    
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private String probableDischargeDate;
    
}
