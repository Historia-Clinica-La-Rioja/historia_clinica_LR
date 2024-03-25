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
    private goesInsideSource = new Subject<boolean>();
	private _goesInside$ = this.goesInsideSource.asObservable();

    private isEmptySource = new BehaviorSubject<boolean>(true);
	isEmpty$ = this.isEmptySource.asObservable();

    private endOfAnesthesiaRadioGroups: EndOfAnesthesiaRadioGroups = {
        painfulSensitivity: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.PAINFUL_SENSITIVITY'),
        cornealReflex: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.CORNEAL_REFLEX'),
        obeyOrders: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.OBEY_ORDERS'),
        talk: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.TALK'),
        respiratoryDepression: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.RESPIRATORY_DEPRESSION'),
        circulatoryDepression: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.CIRCULATORY_DEPRESSION'),
        vomiting: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.VOMITING'),
        cured: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.CURED'),
        trachealCannula: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.TRACHEAL_CANNULA'),
        pharyngealCannula: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.PHARYNGEAL_CANNULA'),
        goesInside: generateRadioGroupInputData('internaciones.anesthesic-report.end-of-anesthesia-status.sections.GOES_INSIDE'),
    }

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

    getEndOfAnesthesiaRadioGroups(): EndOfAnesthesiaRadioGroups {
        return this.endOfAnesthesiaRadioGroups;
    }

    getPostAnesthesiaStatusDto(note?: string): PostAnesthesiaStatusDto {
        return {
            intentionalSensitivity: this.form.value.painfulSensitivity,
            cornealReflex: this.form.value.cornealReflex,
            obeyOrders: this.form.value.obeyOrders,
            talk: this.form.value.talk,
            respiratoryDepression: this.form.value.respiratoryDepression,
            circulatoryDepression: this.form.value.circulatoryDepression,
            vomiting: this.form.value.vomiting,
            curated: this.form.value.cured,
            trachealCannula: this.form.value.trachealCannula,
            pharyngealCannula: this.form.value.pharyngealCannula,
            internment: this.form.value.goesInside,
            internmentPlace: this.form.value.goesInsideOptions,
            note: note,
        }
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
            goesInsideOptions: this.mapToEInternmentPlace(this.form.value.goesInsideOptions)
        }
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
    goesInsideOptions: EInternmentPlace;
}

export enum END_OF_ANESTHESIA_STATUS_OPTIONS {
    YES = 1,
    NO = 2,
}

export enum INTERNMENT_OPTIONS {
    FLOOR = 0,
    INTENSIVE_CARE_UNIT = 1,
}

export interface EndOfAnesthesiaRadioGroups {
    painfulSensitivity: RadioGroupInputData,
    cornealReflex: RadioGroupInputData,
    obeyOrders: RadioGroupInputData,
    talk: RadioGroupInputData,
    respiratoryDepression: RadioGroupInputData,
    circulatoryDepression: RadioGroupInputData,
    vomiting: RadioGroupInputData,
    cured: RadioGroupInputData,
    trachealCannula: RadioGroupInputData,
    pharyngealCannula: RadioGroupInputData,
    goesInside: RadioGroupInputData,
}