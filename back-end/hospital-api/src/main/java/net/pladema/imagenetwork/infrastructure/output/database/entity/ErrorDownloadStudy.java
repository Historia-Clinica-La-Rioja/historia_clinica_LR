package net.pladema.imagenetwork.infrastructure.output.database.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "study_download_error")
@Entity
public class ErrorDownloadStudy {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "image_id", nullable = false, length = 40)
    private String studyInstanceUID;

    @Column(name = "pac_server_id", nullable = false)
    private Integer pacServerId;

    @Column(name = "error_code", nullable = false)
    private Short errorCode;

    @Column(name = "error_description", nullable = false)
    private String errorCodeDescription;

    @Column(name = "effective_time", nullable = false)
    private LocalDateTime effectiveTime;

    @Column(name = "institution_id", nullable = false)
    private Integer institutionId;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "file_uuid", nullable = false)
    private String fileUuid;
}
