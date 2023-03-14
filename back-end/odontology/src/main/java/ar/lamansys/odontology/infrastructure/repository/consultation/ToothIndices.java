package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.EOdontologyIndexBo;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.ToothIndicesBo;
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
@Table(name = "tooth_indices")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ToothIndices {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "tooth_id", nullable = false, length = 20)
	private String toothId;

    @Column(name = "temporary", nullable = false)
    private boolean temporary;

    @Column(name = "whole_tooth", length = 10)
    private String wholeTooth;

    @Column(name = "internal_surface", length = 10)
    private String internalSurface;

    @Column(name = "external_surface", length = 10)
    private String externalSurface;

    @Column(name = "central_surface", length = 10)
    private String centralSurface;

    @Column(name = "left_surface", length = 10)
    private String leftSurface;

    @Column(name = "right_surface", length = 10)
    private String rightSurface;

    public ToothIndices(Integer patientId, ToothIndicesBo toothIndicesBo) {
        this.patientId = patientId;
		this.toothId = toothIndicesBo.getToothId();
        this.temporary = toothIndicesBo.isTemporary();
        this.wholeTooth = mapValue(toothIndicesBo.getWholeToothIndex());
        this.internalSurface = mapValue(toothIndicesBo.getInternalIndex());
        this.externalSurface = mapValue(toothIndicesBo.getExternalIndex());
        this.centralSurface = mapValue(toothIndicesBo.getCentralIndex());
        this.leftSurface = mapValue(toothIndicesBo.getLeftIndex());
        this.rightSurface = mapValue(toothIndicesBo.getRightIndex());
    }

    private static final String CAVITIES_VALUE = "CAVITIES";
    private static final String LOST_VALUE = "LOST";
    private static final String FIXED_VALUE = "FIXED";

    private String mapValue(EOdontologyIndexBo index) {
        switch (index) {
            case CAVITIES:
                return CAVITIES_VALUE;
            case LOST:
                return LOST_VALUE;
            case FIXED:
                return FIXED_VALUE;
            default:
                return null;
        }
    }

    public ToothIndicesBo toToothIndicesBo() {
        ToothIndicesBo result = new ToothIndicesBo(this.getToothId(), this.temporary);
        result.setWholeToothIndex(mapValue(this.wholeTooth));
        result.setInternalIndex(mapValue(this.internalSurface));
        result.setExternalIndex(mapValue(this.externalSurface));
        result.setCentralIndex(mapValue(this.centralSurface));
        result.setLeftIndex(mapValue(this.leftSurface));
        result.setRightIndex(mapValue(this.rightSurface));
        return result;
    }

    private EOdontologyIndexBo mapValue(String value) {
        if (value == null)
            return EOdontologyIndexBo.NONE;
        switch (value) {
            case CAVITIES_VALUE:
                return EOdontologyIndexBo.CAVITIES;
            case LOST_VALUE:
                return EOdontologyIndexBo.LOST;
            case FIXED_VALUE:
                return EOdontologyIndexBo.FIXED;
            default:
                return EOdontologyIndexBo.NONE;
        }
    }
}

