package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

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
@Table(name = "document_post_anesthesia_status")
@Entity
public class DocumentPostAnesthesiaStatus {

    @Id
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "intentional_sensitivity")
    private Boolean intentionalSensitivity;

    @Column(name = "corneal_reflex")
    private Boolean cornealReflex;

    @Column(name = "obey_orders")
    private Boolean obeyOrders;

    @Column(name = "talk")
    private Boolean talk;

    @Column(name = "respiratory_depression")
    private Boolean respiratoryDepression;

    @Column(name = "circulatory_depression")
    private Boolean circulatoryDepression;

    @Column(name = "vomiting")
    private Boolean vomiting;

    @Column(name = "curated")
    private Boolean curated;

    @Column(name = "tracheal_cannula")
    private Boolean trachealCannula;

    @Column(name = "pharyngeal_cannula")
    private Boolean pharyngealCannula;

    @Column(name = "internment")
    private Boolean internment;

    @Column(name = "internment_place_id")
    private Short internmentPlaceId;

    @Column(name = "note_id")
    private Long noteId;
}
