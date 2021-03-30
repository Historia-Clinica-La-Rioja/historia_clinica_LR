import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
	name: 'personIdentification'
})
export class PersonIdentificationPipe implements PipeTransform {

	transform(person: PersonIdentification, ...args: unknown[]): string {
		let personIDString: string = [person.lastName, person.firstName].filter(prop => prop).join(', ');
		if (person.identificationNumber) {
			personIDString = personIDString ? `${personIDString} - ${person.identificationNumber}` : `${person.identificationNumber}`;
		}
		return personIDString;
	}

}

export interface PersonIdentification {
	firstName?: string;
	lastName?: string;
	identificationNumber?: string;
}
