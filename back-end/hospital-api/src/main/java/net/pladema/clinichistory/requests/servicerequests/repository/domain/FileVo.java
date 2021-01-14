package net.pladema.clinichistory.requests.servicerequests.repository.domain;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileVo {
    private Integer fileId;
    private String fileName;
}
