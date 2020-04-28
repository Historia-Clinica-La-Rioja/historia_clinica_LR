export const VITAL_SIGNS_COLUMNS = [
	{
		def: 'heartRate',
		header: 'internaciones.anamnesis.vital-signs.table.columns.HEART_RATE',
		text: ap => ap.heartRate
	},
	{
		def: 'respiratoryRate',
		header: 'internaciones.anamnesis.vital-signs.table.columns.RESPIRATORY_RATE',
		text: ap => ap.respiratoryRate
	},
	{
		def: 'temperature',
		header: 'internaciones.anamnesis.vital-signs.table.columns.TEMPERATURE',
		text: ap => ap.temperature
	},
	{
		def: 'bloodOxygenSaturation',
		header: 'internaciones.anamnesis.vital-signs.table.columns.BLOOD_OXYGEN_SATURATION',
		text: ap => ap.bloodOxygenSaturation
	},
	{
		def: 'systolicBloodPressure',
		header: 'internaciones.anamnesis.vital-signs.table.columns.BLOOD_PRESURE',
		text: ap => `${ap.systolicBloodPressure}/${ap.diastolicBloodPressure}`
	}
];

export const ANTHROPOMETRIC_DATA_COLUMNS = [
	{
		def: 'bloodType',
		header: 'internaciones.anamnesis.datos-antropometricos.BLOOD_TYPE',
		text: ap => ap.bloodType
	},
	{
		def: 'height',
		header: 'internaciones.anamnesis.datos-antropometricos.HEIGHT',
		text: ap => ap.height
	},
	{
		def: 'weight',
		header: 'internaciones.anamnesis.datos-antropometricos.WEIGHT',
		text: ap => ap.weight
	},
	{
		def: 'IMC',
		header: 'internaciones.anamnesis.datos-antropometricos.table.columns.IMC',
		text: ap => Number(ap.weight) / Number(ap.height)
	}
];
