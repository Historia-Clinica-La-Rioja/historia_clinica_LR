export enum MedicationStatus {
    ACTIVE = '55561003',
    SUSPENDED = '385655000',
    STOPPED = '6155003'
}

export enum MedicationStatusChange {
    FINALIZE = 'Finalizar',
    REACTIVATE = 'Reactivar',
    SUSPEND = 'Suspender'
}

export const STUDY_STATUS = {
	REGISTERED: {
		id: "1",
		description: "Pendiente"
	},
	FINAL: {
		id: "261782000",
		description: "Completado"
	},
	ERROR: {
		id: "723510000",
		description: "Eliminado"
	}
};