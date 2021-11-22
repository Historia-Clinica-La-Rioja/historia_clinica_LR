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
	ERROR: {
		id: '89925002',
		description: 'Eliminado'
	}
};
