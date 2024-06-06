package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "procedure_description")
@Entity
public class DocumentProcedureDescription {

    @Id
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "note_id")
    private Long noteId;

    @Column(name = "asa")
    private Short asa;

    @Column(name = "venous_access")
    private Boolean venousAccess;

    @Column(name = "nasogastric_tube")
    private Boolean nasogastricTube;

    @Column(name = "urinary_catheter")
    private Boolean urinaryCatheter;

    @Column(name = "food_intake")
    private LocalTime foodIntake;

    @Column(name = "anesthesia_start_date")
    private LocalDate anesthesiaStartDate;

    @Column(name = "anesthesia_start_time")
    private LocalTime anesthesiaStartTime;

    @Column(name = "anesthesia_end_date")
    private LocalDate anesthesiaEndDate;

    @Column(name = "anesthesia_end_time")
    private LocalTime anesthesiaEndTime;

    @Column(name = "surgery_start_date")
    private LocalDate surgeryStartDate;

    @Column(name = "surgery_start_time")
    private LocalTime surgeryStartTime;

    @Column(name = "surgery_end_date")
    private LocalDate surgeryEndDate;

    @Column(name = "surgery_end_time")
    private LocalTime surgeryEndTime;
}
