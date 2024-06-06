package ar.lamansys.sgh.clinichistory.domain.ips;

import java.time.LocalDate;
import java.time.LocalTime;
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
public class MeasuringPointBo {

    private Integer id;
    private LocalDate date;
    private LocalTime time;
    private Integer bloodPressureMin;
    private Integer bloodPressureMax;
    private Integer bloodPulse;
    private Integer o2Saturation;
    private Integer co2EndTidal;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MeasuringPointBo mp = (MeasuringPointBo) obj;
        return date.equals(mp.getDate()) && time.equals(mp.getTime());
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + time.hashCode();
        return result;
    }
}
