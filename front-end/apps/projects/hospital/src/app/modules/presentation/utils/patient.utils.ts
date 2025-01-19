import { PersonAgeDto } from "@api-rest/api-model";

const ONE = 1;
const MAX_DAYS = 45;
const MAX_MONTHS = 12;

export class PatientBasicData {
	id: number;
	firstName: string;
	middleNames?: string;
	lastName: string;
	otherLastNames?: string;
	gender?: string;
	age?: number;
	nameSelfDetermination?: string;
	selfPerceivedGender?: string;
	personAge?: PersonAgeDto;
}

export function getAge(patient: PatientBasicData): string {
	let age: string = 'presentation.patient.age.NO_AGE';
	if (patient?.personAge) {
		if (patient.personAge.years < ONE) {
			if (patient.personAge.totalDays <= MAX_DAYS){
				age = patient.personAge.days === ONE? 'presentation.patient.age.DAY' : 'presentation.patient.age.DAYS';}
			else
				if (patient.personAge.months <= MAX_MONTHS) {
					if (patient.personAge.months === ONE){
						age = patient.personAge.days === ONE? 'presentation.patient.age.MONTH_AND_DAY' : 'presentation.patient.age.MONTH_AND_DAYS';
					}else
						age = patient.personAge.days === ONE? 'presentation.patient.age.MONTHS_AND_DAY' : 'presentation.patient.age.MONTHS_AND_DAYS';
				}
		}
		else age = patient.personAge.years === ONE? 'presentation.patient.age.YEAR' : 'presentation.patient.age.YEARS';
	}
	return age
}