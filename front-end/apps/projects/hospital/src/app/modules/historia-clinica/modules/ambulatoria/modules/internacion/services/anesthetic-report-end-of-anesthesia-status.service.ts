import { Injectable } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportEndOfAnesthesiaStatusService {

    private form: FormGroup;
    private goesInsideSource = new Subject<boolean>();
	private _goesInside$ = this.goesInsideSource.asObservable();

    private isEmptySource = new BehaviorSubject<boolean>(true);
	isEmpty$ = this.isEmptySource.asObservable();

    constructor() {
        this.form = new FormGroup<IntrasurgicalAnestheticProceduresForm>({
            painfulSensitivity: new FormControl(null),
            cornealReflex: new FormControl(null),
            obeyOrders: new FormControl(null),
            talk: new FormControl(null),
            respiratoryDepression: new FormControl(null),
            circulatoryDepression: new FormControl(null),
            vomiting: new FormControl(null),
            cured: new FormControl(null),
            trachealCannula: new FormControl(null),
            pharyngealCannula: new FormControl(null),
            goesInside: new FormControl(null),
            goesInsideOptions: new FormControl(null),
        });
    }

    getGoesInside(): Observable<boolean> {
		return this._goesInside$;
	}

    setFormAttributeValue(attribute: string, value: END_OF_ANESTHESIA_STATUS_OPTIONS) {
        this.form.get(attribute).setValue(value);
        this.isEmptySource.next(false)
        this.checkGoesInsideValue(attribute, value);
    }

    private checkGoesInsideValue(attribute: string, value: END_OF_ANESTHESIA_STATUS_OPTIONS) {
        if (attribute === 'goesInside') {
            this.goesInsideSource.next(value === END_OF_ANESTHESIA_STATUS_OPTIONS.YES)
        }
    }

    setGoesInsideOptions(goesInsideOptions: INTERNMENT_OPTIONS){
        this.form.get("goesInsideOptions").setValue(goesInsideOptions);
    }

    getIntrasurgicalAnestheticProceduresData(): IntrasurgicalAnestheticProceduresData {
        return {
            painfulSensitivity: this.form.value.painfulSensitivity,
            cornealReflex: this.form.value.cornealReflex,
            obeyOrders: this.form.value.obeyOrders,
            talk: this.form.value.talk,
            respiratoryDepression: this.form.value.respiratoryDepression,
            circulatoryDepression: this.form.value.circulatoryDepression,
            vomiting: this.form.value.vomiting,
            cured: this.form.value.cured,
            trachealCannula: this.form.value.trachealCannula,
            pharyngealCannula: this.form.value.pharyngealCannula,
            goesInside: this.form.value.goesInside,
            goesInsideOptions: this.form.value.goesInsideOptions
        }
    }

    getForm(): FormGroup {
        return this.form;
    }

    isEmpty(): boolean {
        for (let controlName in this.form.controls) {
            if (this.form.controls.hasOwnProperty(controlName)) {
                if (this.form.controls[controlName].value) {
                    return false;
                }
            }
        }
        return true;
    }    
}

export interface IntrasurgicalAnestheticProceduresForm{
    painfulSensitivity: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    cornealReflex: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    obeyOrders: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    talk: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    respiratoryDepression: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    circulatoryDepression: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    vomiting: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    cured: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    trachealCannula: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    pharyngealCannula: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    goesInside: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    goesInsideOptions: FormControl<INTERNMENT_OPTIONS>;
}

export interface IntrasurgicalAnestheticProceduresData {
    painfulSensitivity: END_OF_ANESTHESIA_STATUS_OPTIONS;
    cornealReflex: END_OF_ANESTHESIA_STATUS_OPTIONS;
    obeyOrders: END_OF_ANESTHESIA_STATUS_OPTIONS;
    talk: END_OF_ANESTHESIA_STATUS_OPTIONS;
    respiratoryDepression: END_OF_ANESTHESIA_STATUS_OPTIONS;
    circulatoryDepression: END_OF_ANESTHESIA_STATUS_OPTIONS;
    vomiting: END_OF_ANESTHESIA_STATUS_OPTIONS;
    cured: END_OF_ANESTHESIA_STATUS_OPTIONS;
    trachealCannula: END_OF_ANESTHESIA_STATUS_OPTIONS;
    pharyngealCannula: END_OF_ANESTHESIA_STATUS_OPTIONS;
    goesInside: END_OF_ANESTHESIA_STATUS_OPTIONS;
    goesInsideOptions: INTERNMENT_OPTIONS;
}

export enum END_OF_ANESTHESIA_STATUS_OPTIONS {
    YES = 1,
    NO = 2,
}

export enum INTERNMENT_OPTIONS {
    FLOOR = 1,
    INTENSIVE_CARE_UNIT = 2,
}