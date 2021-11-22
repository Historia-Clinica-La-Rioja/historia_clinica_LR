import {PatientBasicData} from "@presentation/components/patient-card/patient-card.component";

export const PATIENT_TYPE = {
	PERMANENT: 1, // Paso por el servicio de Federar
	VALID: 2, // Datos traidos desde Renaper
	TEMPORARY: 3, // Paciente agregado sin el minimo de informacion necesaria
	HISTORICAL: 4, //
	TELEPHONE: 5, // Agregado por turno telefonico
	REJECTED: 6, //
	PERMANENT_INVALID: 7 // Sin pasar por Federar
};

export const IDENTIFICATION_TYPE_IDS = {
	DNI: 1,
	NO_POSEE: 11
};


export function patientCompleteName(patientBasicData: PatientBasicData): string {
	if(patientBasicData.firstName != null && patientBasicData.lastName != null) {
		return patientBasicData.firstName.concat("_", patientBasicData.lastName);
	} else if (patientBasicData.firstName != null && patientBasicData.lastName == null) {
		return patientBasicData.firstName;
	} else if (patientBasicData.firstName == null && patientBasicData.lastName != null) {
		return patientBasicData.lastName;
	} else {
		return 'Sin información';
	}
}
