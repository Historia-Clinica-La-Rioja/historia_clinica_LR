package net.pladema.clinichistory.requests.servicerequests.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.sgx.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;


@Entity
@Table(name = "diagnostic_report_file")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor

public class DiagnosticReportFile extends SGXAuditableEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name="size", nullable = false)
    private long size;

    @Column(name="diagnostic_report_id")
    private Integer diagnosticReportId;

    public DiagnosticReportFile(String path, String contentType, long size){
        super();
        this.path = path;
        this.contentType = contentType;
        this.size = size;
    }
}
