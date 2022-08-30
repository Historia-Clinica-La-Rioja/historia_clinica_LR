package ar.lamansys.sgh.publicapi.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InternmentBo {

    private String id;
    private LocalDateTime entryDate;
    private LocalDateTime dischargeDate;
}
