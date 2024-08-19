package net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "order_image_file")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor

public class OrderImageFile extends SGXAuditableEntity<Integer> {

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

    @Column(name="order_id")
    private Integer orderId;

    @Column(name="name", nullable = false)
    private String name;

    public OrderImageFile(String path, String contentType, long size, String name, Integer orderId){
        super();
        this.path = path;
        this.contentType = contentType;
        this.size = size;
        this.name = name;
		this.orderId = orderId;
    }
}
