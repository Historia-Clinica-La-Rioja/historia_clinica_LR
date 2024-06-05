export const MEDICATION_STATUS = {
	ACTIVE: {
		id: '55561003',
		description: 'Activa'
	},
	SUSPENDED: {
		id: '385655000',
		description: 'Suspendida'
	},
	STOPPED: {
		id: '6155003',
		description: 'Finalizada'
	}
};

export enum MedicationStatusChange {
	FINALIZE = 'Finalizar',
	REACTIVATE = 'Reactivar',
	SUSPEND = 'Suspender'
}

export const STUDY_STATUS = {
	REGISTERED: {
		id: '1',
		description: 'Pendiente'
	},
	FINAL: {
		id: '261782000',
		description: 'Completado'
	},
	FINAL_RDI: {
		id: '2',
		description: 'Completado'
	},
	PARTIAL: {
		id: '255609007',
		description: 'Completado Parcialmente'
	},
	ERROR: {
		id: '89925002',
		description: 'Eliminado'
	}
};

export const PRESCRIPTION_STATES = {
	INDICADA: {
		id: 1,
		description: 'Indicada'
	},
	ANULADA: {
		id: 6,
		description: 'Anulada'
	},
	DISPENSADA: {
		id: 2,
		description: 'Dispensada'
	},
	PROVISORIO_DISPENSADA: {
		id: 3,
		description: 'Dispensado provisorio'
	}
}
