
const htmlDraws = {
	red_circle: ['398981000221101', '44764005', '306740006', '306740006', '787881007', '468254001',
		'399271000221103', '399301000221101', '398871000221107', '285065008', '399121000221106'],
	short_vertical_red_line: ['789147006'],
	two_long_blue_lines: ['404198007', '173291009', '401261000221100'],
	red_cross: ['16958000', '9984005', '109531008', '37320007', '441935006', '1085521000119100', '109673006', '234948008', '110338002'],
	blue_horizontal_line: ['699685006'],
	blue_oval: ['401271000221109'],
	red_horizontal_line: ['234713009', '399191000221108'],
	red_cross_and_red_vertical_line: ['278123008'],
	TC: ['399001000221103', '399011000221100'],
	red_long_line: ['469111006', '398761000221105', '469108005', '468782009', '285071002', '398791000221100', '468785006'],
	red_circle_and_vertical_line: ['468936006'],
	red_blue_circles: ['278599006'],
	ABSC: ['74598008', '109602002', '109603007', '109610001', '83412009'],
	CdRz: ['234975001'],
	PM: ['398721000221102', '398731000221104', '398821000221106']
};

export const getHtmlName = (sctid: string): string => {
	const htmlName = Object.keys(htmlDraws).filter(key => {
		if (htmlDraws[key].includes(sctid)) {
			return key;
		}
	});
	return htmlName ? htmlName.pop() : undefined;
};

const surfacesCenterPoint = {
	left: {
		x: 5,
		y: 48
	},
	right: {
		x: 35,
		y: 48
	},
	central: {
		x: 20,
		y: 48
	},
	external: {
		x: 20,
		y: 33
	},
	internal: {
		x: 20,
		y: 66
	},

};

export const getDraw = (toothPartId: string, svgName: string) => {
	const height = Number(document.getElementById(svgName).attributes.getNamedItem('height').nodeValue);
	const width = Number(document.getElementById(svgName).attributes.getNamedItem('width').nodeValue);
	//alto y ancho del dibujo que quiero agregar

	const newDrawNodes: HTMLElement[] = [];
	document.getElementById(svgName).childNodes.forEach(nodes => {
		const nodeToAdd = nodes.cloneNode() as HTMLElement;
		const toTransformX = surfacesCenterPoint[toothPartId].x - (width / 2);
		const toTransformY = surfacesCenterPoint[toothPartId].y - (height / 2) + moveCenter(svgName);
		nodeToAdd.style.transform = `translate(${toTransformX}px,${toTransformY}px)`;
		newDrawNodes.push(nodeToAdd);
	});

	return newDrawNodes;
};

const moveCenter = (svgName: string): number => {
	let toTransformY = 0;
	switch (svgName) {
		case 'red_cross_and_red_vertical_line':
			toTransformY -= 19.5;
			break;
		case 'short_vertical_red_line':
		case 'ABSC':
		case 'CdRz':
		case 'PM':
		case 'TC':
		case 'red_long_line':
			toTransformY -= 37.5;
			break;
		case 'red_circle_and_vertical_line':
			toTransformY -= 14.6;
			break;
	}
	return toTransformY;
}

enum Actions {
	FILL = 'fill',
	STROKE = 'stroke'
}

enum Colors {
	RED = '#F18989',
	BLUE = '#67ABD6',
}


const colorsInSurface: { toDo: ActionAndColor[], ids: string[] }[] = [
	{
		toDo: [{
			action: Actions.FILL,
			color: Colors.RED
		}, {
			action: Actions.STROKE,
			color: Colors.BLUE
		}],
		ids: ['702645001', '702745006', '711469000', '716294003', '716296001', '278549007', '112481000119103',
			'109728009', '109730006', '109734002', '109735001', '109736000', '109741008', '109743006']
	},
	{
		toDo: [{ action: Actions.FILL, color: Colors.RED }],
		ids: ['399031000221108', '399041000221101', '399051000221104', '399061000221102',
			'399071000221106', '399081000221109', '80764003', '57673005', '398891000221108', '399131000221109', '4721000221105', '398861000221103']
	},
	{
		toDo: [{ action: Actions.FILL, color: Colors.BLUE }],
		ids: ['109564008', '109566005', '109568006', '733971007', '733973005', '733975003', '733977006', '700046006',
			'733978001', '733980007', '733982004', '767855004', '109573000', '234975001', '80967001', '95246007',
			'95247003', '95249000', '109564008', '109566005', '109568006', '109569003', '234976000', '702402003',
			'1085521000119100', '109569003', '95246007', '95247003', '95249000', '416673002']
	}
];

export interface ActionAndColor {
	action: Actions;
	color: Colors;
}
export const getPaint = (sctid: string): ActionAndColor[] => {
	return sctid ? colorsInSurface.find(s => s.ids.includes(sctid))?.toDo : null;
};
