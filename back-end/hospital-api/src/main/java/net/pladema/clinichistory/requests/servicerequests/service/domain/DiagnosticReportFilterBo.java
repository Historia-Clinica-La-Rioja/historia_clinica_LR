package net.pladema.clinichistory.requests.servicerequests.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiagnosticReportFilterBo {
    private Integer patientId;
    private String status;
    private String study;
    private String healthCondition;
    private String category;
    private List<String> categoriesToBeExcluded;
    private Integer userId;
    private List<Short> loggedUserRoleIds;
}
