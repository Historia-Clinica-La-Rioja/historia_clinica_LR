import { ESurfacePositionDto } from "@api-rest/api-model";

/** Depending on each tooth, the names given to each surface */
const surfacesNames = {
	245653007: 'distal',
	245652002: 'mesial',
	245647007: 'vestibular',
	245650005: 'palatina',
	245645004: 'incisal',
	362103001: 'oclusal',
	362104007: 'lingual'
};

export const getSurfaceShortName = (sctid: string) => surfacesNames[sctid];

export const ESurfacePositionDtoValues = {
	internal: ESurfacePositionDto.INTERNAL,
	external: ESurfacePositionDto.EXTERNAL,
	left: ESurfacePositionDto.LEFT,
	right: ESurfacePositionDto.RIGHT,
	central: ESurfacePositionDto.CENTRAL
}
