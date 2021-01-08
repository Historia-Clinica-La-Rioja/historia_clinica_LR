package net.pladema.clinichistory.requests.servicerequests.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;


@Entity
@Table(name = "diagnostic_report_file")
@Getter
@Setter
@ToString
@NoArgsConstructor

public class DiagnosticReportFileMetadata {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name="size", nullable = false)
    private Integer size;
}
