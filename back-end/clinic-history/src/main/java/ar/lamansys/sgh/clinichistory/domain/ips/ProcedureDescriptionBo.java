package ar.lamansys.sgh.clinichistory.domain.ips;

import java.time.LocalDate;
import java.time.LocalTime;

import ar.lamansys.sgh.clinichistory.domain.ips.visitor.IpsVisitor;
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
public class ProcedureDescriptionBo implements IpsBo {

    private Long id;
    private String note;
    private Short asa;
    private Boolean venousAccess;
    private Boolean nasogastricTube;
    private Boolean urinaryCatheter;
    private LocalTime foodIntake;
    private LocalDate anesthesiaStartDate;
    private LocalTime anesthesiaStartTime;
    private LocalDate anesthesiaEndDate;
    private LocalTime anesthesiaEndTime;
    private LocalDate surgeryStartDate;
    private LocalTime surgeryStartTime;
    private LocalDate surgeryEndDate;
    private LocalTime surgeryEndTime;

    @Override
    public void accept(IpsVisitor visitor) {
        visitor.visitProcedureDescription(this);
    }
}
