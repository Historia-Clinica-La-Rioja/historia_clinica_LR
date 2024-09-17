package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "last_odontogram_drawing")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LastOdontogramDrawing {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "tooth_id", nullable = false, length = 20)
	private String toothId;

    @Column(name = "whole_tooth", length = 20)
    private String wholeTooth;

    @Column(name = "internal_surface", length = 20)
    private String internalSurface;

    @Column(name = "external_surface", length = 20)
    private String externalSurface;

    @Column(name = "central_surface", length = 20)
    private String centralSurface;

    @Column(name = "left_surface", length = 20)
    private String leftSurface;

    @Column(name = "right_surface", length = 20)
    private String rightSurface;

	@Column(name = "odontology_consultation_id")
	private Integer  odontologyConsultationId;

    public LastOdontogramDrawing(Integer patientId, ToothDrawingsBo toothDrawings) {
        this.patientId = patientId;
		this.toothId = toothDrawings.getToothId();
        this.wholeTooth = (toothDrawings.getWholeDrawing() != null) ? toothDrawings.getWholeDrawing().getSnomed().getSctid() : null;
        this.internalSurface = (toothDrawings.getInternalSurfaceDrawing() != null) ? toothDrawings.getInternalSurfaceDrawing().getSctid() : null;
        this.externalSurface = (toothDrawings.getExternalSurfaceDrawing() != null) ? toothDrawings.getExternalSurfaceDrawing().getSctid() : null;
        this.centralSurface = (toothDrawings.getCentralSurfaceDrawing() != null) ? toothDrawings.getCentralSurfaceDrawing().getSctid() : null;
        this.leftSurface = (toothDrawings.getLeftSurfaceDrawing() != null) ? toothDrawings.getLeftSurfaceDrawing().getSctid() : null;
        this.rightSurface = (toothDrawings.getRightSurfaceDrawing() != null) ? toothDrawings.getRightSurfaceDrawing().getSctid() : null;
    }

	public LastOdontogramDrawing(Integer patientId, ToothDrawingsBo toothDrawings, Integer consultationId) {
		this.patientId = patientId;
		this.toothId = toothDrawings.getToothId();
		this.odontologyConsultationId = consultationId;
		this.wholeTooth = (toothDrawings.getWholeDrawing() != null) ? toothDrawings.getWholeDrawing().getSnomed().getSctid() : null;
		this.internalSurface = (toothDrawings.getInternalSurfaceDrawing() != null) ? toothDrawings.getInternalSurfaceDrawing().getSctid() : null;
		this.externalSurface = (toothDrawings.getExternalSurfaceDrawing() != null) ? toothDrawings.getExternalSurfaceDrawing().getSctid() : null;
		this.centralSurface = (toothDrawings.getCentralSurfaceDrawing() != null) ? toothDrawings.getCentralSurfaceDrawing().getSctid() : null;
		this.leftSurface = (toothDrawings.getLeftSurfaceDrawing() != null) ? toothDrawings.getLeftSurfaceDrawing().getSctid() : null;
		this.rightSurface = (toothDrawings.getRightSurfaceDrawing() != null) ? toothDrawings.getRightSurfaceDrawing().getSctid() : null;
	}

}
