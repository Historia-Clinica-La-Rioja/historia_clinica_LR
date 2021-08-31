package net.pladema.sgx.repository;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "auditable_class")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
public class AuditableClass extends SGXAuditableEntity<Integer> {

    @Id
    @Column(name = "id")
    @ToString.Include
    private Integer id;
}