package net.pladema.emergencycare.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.sgx.masterdata.dto.MasterDataDto;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
//Used 'V' to represent a Dto that is used to show info, not to save it
public class VMedicalDischargeDto extends MedicalDischargeDto {

    private MasterDataDto dischargeType;

    private List<String> snomedPtProblems;

}
