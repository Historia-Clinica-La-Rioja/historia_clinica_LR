export const RISK_FACTORS_COLUMNS = [
	{
		def: 'heartRate',
		header: 'internaciones.anamnesis.risk-factors.table.columns.HEART_RATE',
		text: ap => ap.heartRate.description
	},
	{
		def: 'respiratoryRate',
		header: 'internaciones.anamnesis.risk-factors.table.columns.RESPIRATORY_RATE',
		text: ap => ap.respiratoryRate.description
	},
	{
		def: 'temperature',
		header: 'internaciones.anamnesis.risk-factors.table.columns.TEMPERATURE',
		text: ap => ap.temperature.description
	},
	{
		def: 'bloodOxygenSaturation',
		header: 'internaciones.anamnesis.risk-factors.table.columns.BLOOD_OXYGEN_SATURATION',
		text: ap => ap.bloodOxygenSaturation.description
	},
	{
		def: 'systolicBloodPressure',
		header: 'internaciones.anamnesis.risk-factors.table.columns.BLOOD_PRESURE',
		text: ap => `${ap.systolicBloodPressure.description}/${ap.diastolicBloodPressure.description}`
	}
];

export const ANTHROPOMETRIC_DATA_COLUMNS = [
	{
		def: 'bloodType',
		header: 'internaciones.anamnesis.datos-antropometricos.BLOOD_TYPE',
		text: ap => ap.bloodType.description
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
