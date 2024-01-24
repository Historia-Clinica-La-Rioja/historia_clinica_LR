package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

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
@Table(name = "food_intake")
@Entity
public class DocumentFoodInTake {

    @Id
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "clock_time", nullable = false)
    private LocalTime clockTime;
}
