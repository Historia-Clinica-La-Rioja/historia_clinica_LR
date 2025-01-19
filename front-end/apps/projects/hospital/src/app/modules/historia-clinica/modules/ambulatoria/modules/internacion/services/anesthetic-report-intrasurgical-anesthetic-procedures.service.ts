import { Injectable } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ProcedureDescriptionDto } from '@api-rest/api-model';
import { RadioGroupInputData, generateRadioGroupInputData } from '@presentation/components/radio-group/radio-group.component';
import { BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportIntrasurgicalAnestheticProceduresService {

    private form: FormGroup<IntrasurgicalAnestheticProceduresForm>;
	private isEmptySource = new BehaviorSubject<boolean>(true);
	isEmpty$ = this.isEmptySource.asObservable();

    private intrasurgicalAnestheticProceduresRadioGroups: IntresurgicalAnestheticProceduresRadioGroups = {
        venousAccessRadioGroupInputData: generateRadioGroupInputData("internaciones.anesthesic-report.intrasurgical-anesthetic-procedures.VENOUS_ACCESS", null, null, null, "column", "column"),
        nasogastricTubeRadioGroupInputData: generateRadioGroupInputData("internaciones.anesthesic-report.intrasurgical-anesthetic-procedures.NASOGASTRIC_TUBE", null, null, null, "column", "column"),
        urinaryCatheterRadioGroupInputData: generateRadioGroupInputData("internaciones.anesthesic-report.intrasurgical-anesthetic-procedures.URINARY_CATHETER", null, null, null, "column", "column"),
    }

    constructor() {
        this.form = new FormGroup<IntrasurgicalAnestheticProceduresForm>({
            venousAccess: new FormControl(null),
            nasogastricTube: new FormControl(null),
            urinaryCatheter: new FormControl(null),
        });
    }

    getIntrasurgicalAnestheticProceduresRadioGroups(): IntresurgicalAnestheticProceduresRadioGroups {
        return this.intrasurgicalAnestheticProceduresRadioGroups
    }

    setVenousAccess(venousAccess: INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS){
        this.form.get("venousAccess").setValue(venousAccess);
		this.isEmptySource.next(this.isEmpty());
    }

    setNasogastricTube(nasogastricTube: INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS){
        this.form.get("nasogastricTube").setValue(nasogastricTube);
		this.isEmptySource.next(this.isEmpty());
    }

    setUrinaryCatheter(urinaryCatheter: INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS){
        this.form.get("urinaryCatheter").setValue(urinaryCatheter);
		this.isEmptySource.next(this.isEmpty());
    }

	getIntrasurgicalAnestheticProceduresData(): IntrasurgicalAnestheticProceduresData {
		return {
			venousAccess: this.form.value.venousAccess !== null ? this.form.value.venousAccess === INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS.YES : null,
			nasogastricTube: this.form.value.nasogastricTube !== null ? this.form.value.nasogastricTube === INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS.YES : null,
			urinaryCatheter: this.form.value.urinaryCatheter !== null ? this.form.value.urinaryCatheter === INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS.YES : null,
		};
	}

    getForm(): FormGroup {
        return this.form;
    }

    isEmpty(): boolean {
        return !(this.form.value.venousAccess || this.form.value.nasogastricTube || this.form.value.urinaryCatheter);
    }

	setData(intrasurgicalAnestheticProcedures: ProcedureDescriptionDto): void {
		if (intrasurgicalAnestheticProcedures) {
			let venousAccessValue = intrasurgicalAnestheticProcedures?.venousAccess !== undefined
				? (intrasurgicalAnestheticProcedures.venousAccess ? INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS.YES : INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS.NO)
				: null;

			let nasogastricTubeValue = intrasurgicalAnestheticProcedures?.nasogastricTube !== undefined
				? (intrasurgicalAnestheticProcedures.nasogastricTube ? INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS.YES : INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS.NO)
				: null;

			let urinaryCatheterValue = intrasurgicalAnestheticProcedures?.urinaryCatheter !== undefined
				? (intrasurgicalAnestheticProcedures.urinaryCatheter ? INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS.YES : INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS.NO)
				: null;

			this.setVenousAccess(venousAccessValue);
			this.setNasogastricTube(nasogastricTubeValue);
			this.setUrinaryCatheter(urinaryCatheterValue);
		}
	}
}

export interface IntrasurgicalAnestheticProceduresForm{
    venousAccess: FormControl<INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS>;
    nasogastricTube: FormControl<INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS>;
    urinaryCatheter: FormControl<INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS>;
}

export interface IntrasurgicalAnestheticProceduresData {
    venousAccess: boolean;
    nasogastricTube: boolean;
    urinaryCatheter: boolean;
}

export enum INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS {
    YES = 1,
    NO = 2,
}

export interface IntresurgicalAnestheticProceduresRadioGroups {
    venousAccessRadioGroupInputData: RadioGroupInputData,
    nasogastricTubeRadioGroupInputData: RadioGroupInputData,
    urinaryCatheterRadioGroupInputData: RadioGroupInputData,
}
