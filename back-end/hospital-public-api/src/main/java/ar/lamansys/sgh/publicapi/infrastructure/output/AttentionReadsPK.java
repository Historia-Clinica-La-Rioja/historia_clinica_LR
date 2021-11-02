package ar.lamansys.sgh.publicapi.infrastructure.output;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
@EqualsAndHashCode
public class AttentionReadsPK implements Serializable {

    @Column(name = "attention_id", nullable = false)
    private Long attentionId;

    @Column(name = "user_id", nullable = false)
    private String userId;
}
