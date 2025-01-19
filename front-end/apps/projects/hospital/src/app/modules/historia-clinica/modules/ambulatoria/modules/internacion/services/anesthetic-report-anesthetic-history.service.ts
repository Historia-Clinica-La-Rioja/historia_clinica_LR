import { Injectable } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AnestheticHistoryDto } from '@api-rest/api-model';
import { BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportAnestheticHistoryService {

    private form: FormGroup<PreviousAnesthesiaDataForm>;
	private isEmptySource = new BehaviorSubject<boolean>(true);
	isEmpty$ = this.isEmptySource.asObservable();

    constructor() {
        this.form = new FormGroup<PreviousAnesthesiaDataForm>({
            previousAnesthesiaState: new FormControl(null),
            anesthesiaZone: new FormControl(ANESTHESIA_ZONE_ID.REGIONAL),
        });
    }

    getForm(): FormGroup {
        return this.form;
    }

    getAnestheticHistoryData(): AnestheticHistoryDto {
        return {
            stateId: this.form.value.previousAnesthesiaState,
            zoneId: this.form.value.previousAnesthesiaState ? this.form.value.anesthesiaZone : null
        }
    }

    setPreviousAnesthesiaData(previousAnesthesiaState: PREVIOUS_ANESTHESIA_STATE_ID){
		this.isEmptySource.next(false)
        this.form.get("previousAnesthesiaState").setValue(previousAnesthesiaState);
    }

    setAnesthesiaZoneData(anesthesiaZone: ANESTHESIA_ZONE_ID){
        this.form.get("anesthesiaZone").setValue(anesthesiaZone);
    }

	setData(anestheticHistoryData: AnestheticHistoryDto) {
		if (anestheticHistoryData) {
			this.setPreviousAnesthesiaData(anestheticHistoryData.stateId);
			if (anestheticHistoryData.stateId === PREVIOUS_ANESTHESIA_STATE_ID.YES) {
				this.setAnesthesiaZoneData(anestheticHistoryData.zoneId);
			}
		}
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
