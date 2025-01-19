package net.pladema.clinichistory.requests.servicerequests.repository.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileVo {
    private Integer fileId;
    private String fileName;
}
