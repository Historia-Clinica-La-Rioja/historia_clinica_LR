package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.EOdontologyIndexBo;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.ToothIndicesBo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tooth_indices")
@Getter
@NoArgsConstructor
public class ToothIndices {

    @EmbeddedId
    private ToothIndicesPK pk;

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
        this.pk = new ToothIndicesPK(patientId, toothIndicesBo.getToothId());
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
        ToothIndicesBo result = new ToothIndicesBo(this.pk.getToothId(), this.temporary);
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

