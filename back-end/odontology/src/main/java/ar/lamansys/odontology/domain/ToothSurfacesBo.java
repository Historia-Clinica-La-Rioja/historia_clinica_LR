package ar.lamansys.odontology.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ToothSurfacesBo {
    private OdontologySnomedBo left;
    private OdontologySnomedBo right;
    private OdontologySnomedBo internal;
    private OdontologySnomedBo external;
    private OdontologySnomedBo central;

    public OdontologySnomedBo getSurface(ESurfacePositionBo position) {
        switch (position) {
            case EXTERNAL:
                return external;
            case INTERNAL:
                return internal;
            case CENTRAL:
                return central;
            case LEFT:
                return left;
            case RIGHT:
                return right;
            default:
                return null;
        }
    }

	public ESurfacePositionBo getSurfacePosition(OdontologySnomedBo surface) {
		if (surface.equals(left))
			return ESurfacePositionBo.LEFT;
		if (surface.equals(right))
			return ESurfacePositionBo.RIGHT;
		if (surface.equals(central))
			return ESurfacePositionBo.CENTRAL;
		if (surface.equals(external))
			return ESurfacePositionBo.EXTERNAL;
		if (surface.equals(internal))
			return ESurfacePositionBo.INTERNAL;
		return null;
	}

}
