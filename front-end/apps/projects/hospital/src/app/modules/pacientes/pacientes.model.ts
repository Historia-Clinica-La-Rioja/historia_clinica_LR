export class DatosPersonales {
	firstName: string;
	middleNames: string;
	lastName: string;
	otherLastNames: string;
	identificationTypeId: number;
	identificationNumber: string;
	genderId: number;
	birthDate: Date;

	// Person_extended
	cuil: string;
	mothersLastName: string;
	phoneNumber: string;
	email: string;
	ethnic: string;
	religion: string;
	nameSelfDetermination: string;
	genderSelfDeterminationId: number;
	healthInsuranceId: number;

	// Address
	addressStreet: string;
	addressNumber: string;
	addressFloor: string;
	addressApartment: string;
	addressQuarter: string;
	addressCityId: number;
	addressPostcode: string;

}

export class IdentityVerificationStatus {
	id: number;
	description: string;
}

export interface AdditionalInfo {
	description?: string;
	data?: string;
}

export interface PatientInformationScan {
	identifType: number;
	identifNumber: string;
	gender: number;
	firstName: string;
	middleNames: string;
	lastName: string;
	otherLastNames: string;
	birthDate: string;
}