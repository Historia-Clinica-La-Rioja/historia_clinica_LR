import { Injectable } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportAnestheticHistoryService {

    private form: FormGroup;
    private _isEmpty: boolean = true;

    constructor() {
        this.form = new FormGroup<PreviousAnesthesiaDataForm>({
            previousAnesthesiaState: new FormControl(null),
            anesthesiaZone: new FormControl(ANESTHESIA_ZONE_ID.REGIONAL),
        });
    }

    getForm(): FormGroup {
        return this.form;
    }

    setPreviousAnesthesiaData(previousAnesthesiaState: PREVIOUS_ANESTHESIA_STATE_ID){
        this._isEmpty = false;
        this.form.get("previousAnesthesiaState").setValue(previousAnesthesiaState);
    }

    setAnesthesiaZoneData(anesthesiaZone: ANESTHESIA_ZONE_ID){
        this.form.get("anesthesiaZone").setValue(anesthesiaZone);
    }

    isEmpty(): boolean {
        return this._isEmpty;
    }
}

export interface PreviousAnesthesiaDataForm{
    previousAnesthesiaState: FormControl<PREVIOUS_ANESTHESIA_STATE_ID>;
    anesthesiaZone: FormControl<ANESTHESIA_ZONE_ID>;
}

export enum PREVIOUS_ANESTHESIA_STATE_ID {
    YES = 1,
    NO = 2,
    CANT_ANSWER = 3,
}

export enum ANESTHESIA_ZONE_ID {
    REGIONAL = 1,
    GENERAL = 2,
    BOTH = 3,
}