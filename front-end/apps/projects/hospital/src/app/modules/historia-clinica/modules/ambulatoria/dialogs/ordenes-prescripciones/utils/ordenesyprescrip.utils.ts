import {AbstractControl} from "@angular/forms";


export const invalidSimbols = {
	'-': () => '-'
}

export function intervalValidation(form: AbstractControl, control: string, active: string): boolean | null {
	if(form.get(active).value === 0) {
		if (form.get(control) && form.get(control).value) {
			const hours: string = form.get(control).value.toString();
			return isValidInterval(hours);
		}
		return true;
	}
	return null;
}

function isValidInterval(hours: string): boolean {
	for (let invalidSimbolsKey in invalidSimbols) {
		if (hours.includes(invalidSimbolsKey)) {
			return true;
		}
	}
	return false;
}
