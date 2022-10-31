package ar.lamansys.sgh.publicapi.infrastructure.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "attention_reads")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttentionReads {

    @EmbeddedId
    private AttentionReadsPK attentionReadsPK;

    @Column(name = "processed")
    private Boolean processed;

    @Column(name = "performed_date")
    private LocalDateTime performedDate;
}
