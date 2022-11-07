import { Pipe, PipeTransform } from '@angular/core';
import { ERole } from '@api-rest/api-model';
import { PermissionsService } from '@core/services/permissions.service';
import { capitalize } from '@core/utils/core.utils';

const ROLES_THAT_NEED_TO_CHANGE_WORD = [ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE];

const PATIENT = 'paciente';
const PATIENTS = 'pacientes';
const PERSON = 'persona';
const PEOPLE = 'personas';

@Pipe({
	name: 'patientToPerson'
})
export class PatientToPersonPipe implements PipeTransform {
	private roleNeedsToChangeTheWord = false;
	constructor(
		private permissionsService: PermissionsService

	) {
		this.permissionsService.hasContextAssignments$(ROLES_THAT_NEED_TO_CHANGE_WORD).subscribe(e => this.roleNeedsToChangeTheWord = e);
	}

	transform(value: string): string {
		return this.roleNeedsToChangeTheWord ? this.convert(value) : value;
	}
	private convert(value: string): string {
		if (value.includes(`${capitalize(PATIENT)}`))
			return value.replace(`${capitalize(PATIENT)}`, `${capitalize(PERSON)}`)
		if (value.includes(PATIENT))
			return value.replace(PATIENT, PERSON)
		if (value.includes(`${capitalize(PATIENTS)}`))
			return value.replace(`${capitalize(PATIENTS)}`, `${capitalize(PERSON)}`)
		if (value.includes(PATIENTS))
			return value.replace(PATIENTS, PEOPLE)
		return value
	}

}
