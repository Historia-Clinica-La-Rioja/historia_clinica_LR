import { Injectable } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { EInternmentPlace, PostAnesthesiaStatusDto } from '@api-rest/api-model';
import { RadioGroupInputData, generateRadioGroupInputData } from '@presentation/components/radio-group/radio-group.component';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportEndOfAnesthesiaStatusService {

    private form: FormGroup;
    private internmentSource = new Subject<boolean>();
	private _internment$ = this.internmentSource.asObservable();
    private isEmptySource = new BehaviorSubject<boolean>(true);
	isEmpty$ = this.isEmptySource.asObservable();

    private endOfAnesthesiaRadioGroups: EndOfAnesthesiaRadioGroups = {
        intentionalSensitivity: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.PAINFUL_SENSITIVITY'),
        cornealReflex: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.CORNEAL_REFLEX'),
        obeyOrders: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.OBEY_ORDERS'),
        talk: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.TALK'),
        respiratoryDepression: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.RESPIRATORY_DEPRESSION'),
        circulatoryDepression: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.CIRCULATORY_DEPRESSION'),
        vomiting: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.VOMITING'),
        curated: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.CURED'),
        trachealCannula: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.TRACHEAL_CANNULA'),
        pharyngealCannula: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.PHARYNGEAL_CANNULA'),
        internment: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.GOES_INSIDE'),
    }

    constructor() {
        this.form = new FormGroup<IntrasurgicalAnestheticProceduresForm>({
            intentionalSensitivity: new FormControl(null),
            cornealReflex: new FormControl(null),
            obeyOrders: new FormControl(null),
            talk: new FormControl(null),
            respiratoryDepression: new FormControl(null),
            circulatoryDepression: new FormControl(null),
            vomiting: new FormControl(null),
            curated: new FormControl(null),
            trachealCannula: new FormControl(null),
            pharyngealCannula: new FormControl(null),
            internment: new FormControl(null),
            internmentOptions: new FormControl(null),
        });
    }

    getEndOfAnesthesiaRadioGroups(): EndOfAnesthesiaRadioGroups {
        return this.endOfAnesthesiaRadioGroups;
    }

    getPostAnesthesiaStatusDto(note?: string): PostAnesthesiaStatusDto {
        return {
            internmentPlace: this.mapToEInternmentPlace(this.form.value.internmentOptions),
            note: note,
            ...this.form.value,
        }
    }

    getinternment(): Observable<boolean> {
		return this._internment$;
	}

    setFormAttributeValue(attribute: AnestheticProcedureAttribute, value: END_OF_ANESTHESIA_STATUS_OPTIONS) {
        this.form.get(attribute).setValue(value);
        this.isEmptySource.next(false)
        if (attribute === 'internment') {
            this.internmentSource.next(value === END_OF_ANESTHESIA_STATUS_OPTIONS.YES)
        }
    }

    setinternmentOptions(internmentOptions: INTERNMENT_OPTIONS){
        this.form.get("internmentOptions").setValue(internmentOptions);
    }

    private mapToEInternmentPlace(optionSelected: number): EInternmentPlace {
        if (optionSelected === INTERNMENT_OPTIONS.FLOOR) return EInternmentPlace.FLOOR
        if (optionSelected === INTERNMENT_OPTIONS.INTENSIVE_CARE_UNIT) return EInternmentPlace.INTENSIVE_CARE_UNIT
        return null
    }

    getForm(): FormGroup {
        return this.form;
    }  
}

export type AnestheticProcedureAttribute = keyof IntrasurgicalAnestheticProceduresForm;

export interface IntrasurgicalAnestheticProceduresForm{
    intentionalSensitivity: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    cornealReflex: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    obeyOrders: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    talk: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    respiratoryDepression: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    circulatoryDepression: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    vomiting: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    curated: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    trachealCannula: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    pharyngealCannula: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    internment: FormControl<END_OF_ANESTHESIA_STATUS_OPTIONS>;
    internmentOptions: FormControl<INTERNMENT_OPTIONS>;
}

export interface IntrasurgicalAnestheticProceduresData {
    intentionalSensitivity: END_OF_ANESTHESIA_STATUS_OPTIONS;
    cornealReflex: END_OF_ANESTHESIA_STATUS_OPTIONS;
    obeyOrders: END_OF_ANESTHESIA_STATUS_OPTIONS;
    talk: END_OF_ANESTHESIA_STATUS_OPTIONS;
    respiratoryDepression: END_OF_ANESTHESIA_STATUS_OPTIONS;
    circulatoryDepression: END_OF_ANESTHESIA_STATUS_OPTIONS;
    vomiting: END_OF_ANESTHESIA_STATUS_OPTIONS;
    curated: END_OF_ANESTHESIA_STATUS_OPTIONS;
    trachealCannula: END_OF_ANESTHESIA_STATUS_OPTIONS;
    pharyngealCannula: END_OF_ANESTHESIA_STATUS_OPTIONS;
    internment: END_OF_ANESTHESIA_STATUS_OPTIONS;
    internmentOptions: EInternmentPlace;
}

export enum END_OF_ANESTHESIA_STATUS_OPTIONS {
    NO = 0,
    YES = 1,
}

export enum INTERNMENT_OPTIONS {
    FLOOR = 1,
    INTENSIVE_CARE_UNIT = 2,
}

export interface EndOfAnesthesiaRadioGroups {
    intentionalSensitivity: RadioGroupInputData,
    cornealReflex: RadioGroupInputData,
    obeyOrders: RadioGroupInputData,
    talk: RadioGroupInputData,
    respiratoryDepression: RadioGroupInputData,
    circulatoryDepression: RadioGroupInputData,
    vomiting: RadioGroupInputData,
    curated: RadioGroupInputData,
    trachealCannula: RadioGroupInputData,
    pharyngealCannula: RadioGroupInputData,
    internment: RadioGroupInputData,
}