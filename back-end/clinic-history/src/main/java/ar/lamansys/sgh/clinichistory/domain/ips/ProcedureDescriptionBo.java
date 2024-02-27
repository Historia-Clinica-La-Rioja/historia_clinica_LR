package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProcedureDescriptionBo {

    private Long id;
    private String note;
    private Short asa;
    private Boolean venousAccess;
    private Boolean nasogastricTube;
    private Boolean urinaryCatheter;
}
