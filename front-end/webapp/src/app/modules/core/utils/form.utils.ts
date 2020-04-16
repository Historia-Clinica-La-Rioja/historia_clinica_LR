import { FormGroup } from "@angular/forms";

export const VALIDATIONS = {
	MAX_LENGTH: {
		identif_number: 11,
	}
}

export function hasError(form: FormGroup, type: string, control: string): boolean {
	return form.get(control).hasError(type);
}
