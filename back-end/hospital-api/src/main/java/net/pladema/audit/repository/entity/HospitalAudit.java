package net.pladema.audit.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "hospital_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HospitalAudit implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1703592032595709753L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "institution_id")
    private Integer institutionId;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "action_type", nullable = false)
    private Short actionType;

}