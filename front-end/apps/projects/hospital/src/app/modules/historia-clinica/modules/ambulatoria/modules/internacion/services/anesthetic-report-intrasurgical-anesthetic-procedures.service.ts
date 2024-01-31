import { Injectable } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportIntrasurgicalAnestheticProceduresService {

    private form: FormGroup;

    constructor() {
        this.form = new FormGroup<IntrasurgicalAnestheticProceduresForm>({
            venousAccess: new FormControl(null),
            nasogastricTube: new FormControl(null),
            urinaryCatheter: new FormControl(null),
        });
    }

    setVenousAccess(venousAccess: INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS){
        this.form.get("venousAccess").setValue(venousAccess);
    }

    setNasogastricTube(nasogastricTube: INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS){
        this.form.get("nasogastricTube").setValue(nasogastricTube);
    }

    setUrinaryCatheter(urinaryCatheter: INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS){
        this.form.get("urinaryCatheter").setValue(urinaryCatheter);
    }

    getIntrasurgicalAnestheticProceduresData(): IntrasurgicalAnestheticProceduresData {
        return {
            venousAccess: this.form.value.venousAccess,
            nasogastricTube: this.form.value.nasogastricTube,
            urinaryCatheter: this.form.value.urinaryCatheter
        }
    }

    getForm(): FormGroup {
        return this.form;
    }

    isEmpty(): boolean {
        return !(this.form.value.venousAccess || this.form.value.nasogastricTube || this.form.value.urinaryCatheter);
    }
}

export interface IntrasurgicalAnestheticProceduresForm{
    venousAccess: FormControl<INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS>;
    nasogastricTube: FormControl<INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS>;
    urinaryCatheter: FormControl<INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS>;
}

export interface IntrasurgicalAnestheticProceduresData {
    venousAccess: INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS;
    nasogastricTube: INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS;
    urinaryCatheter: INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS;
}

export enum INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS {
    YES = 1,
    NO = 2,
}