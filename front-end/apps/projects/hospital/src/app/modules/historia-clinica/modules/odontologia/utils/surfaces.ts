/** Depending on each tooth, the names given to each surface */
const surfacesNames = {
	64881354351: 'distal',
	658813411: 'mesial',
	116881435: 'vesticular',
	884613511: 'palatina',
	135468811: 'incisal'
};

export const getSurfaceShortName = (sctid: number) => surfacesNames[sctid];
