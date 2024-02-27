import { Injectable } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { TimeDto } from '@api-rest/api-model';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportVitalSignsService {

    private form: FormGroup;
    private posibleTimes: TimeDto[] = [];
    private readonly possibleMinutes = [0,5,10,15,20,25,30,35,40,45,50,55];

    constructor() { 
        this.form = new FormGroup<MeasuringPointForm>({
            measuringPointStartDate: new FormControl(null),
            measuringPointStartTime: new FormControl(null),
            bloodPressureMax: new FormControl(null),
            bloodPressureMin: new FormControl(null),
            pulse: new FormControl(null),
            saturation: new FormControl(null),
            endTidal: new FormControl(null),
        });
        
        this.posibleTimes = this.generateInitialTimes();
    }

    private generateInitialTimes(): TimeDto[] {
		const initializedTimes = [];
		for (let currentHour = 0; currentHour < 24; currentHour++) {
            for (let minute of this.possibleMinutes){
                initializedTimes.push({
                    hours: currentHour,
                    minutes: minute
                });
            }
		}
		return initializedTimes;
	}

    getPossibleTimesList(): TimeDto[] {
        return this.posibleTimes;
    }

    getForm(): FormGroup {
        return this.form;
    }

    setMeasuringPointData(data: MeasuringPointData) {
        data.measuringPointStartDate ? this.form.value.measuringPointStartDate.setValue(data.measuringPointStartDate) : null ;
        data.measuringPointStartTime ? this.form.value.measuringPointStartTime.setValue(data.measuringPointStartTime) : null ;
        data.bloodPressureMax ? this.form.value.bloodPressureMax.setValue(data.bloodPressureMax) : null ;
        data.bloodPressureMin ? this.form.value.bloodPressureMin.setValue(data.bloodPressureMin) : null ;
        data.pulse ? this.form.value.pulse.setValue(data.pulse) : null ;
        data.saturation ? this.form.value.saturation.setValue(data.saturation) : null ;
        data.endTidal ? this.form.value.endTidal.setValue(data.endTidal) : null ;
    }
}

export interface MeasuringPointForm {
    measuringPointStartDate: FormControl<Date>;
    measuringPointStartTime: FormControl<TimeDto>;
    bloodPressureMax: FormControl<number>;
    bloodPressureMin: FormControl<number>;
    pulse: FormControl<number>;
    saturation: FormControl<number>;
    endTidal: FormControl<number>;
}

export interface MeasuringPointData {
    measuringPointStartDate: Date;
    measuringPointStartTime: TimeDto;
    bloodPressureMax: number;
    bloodPressureMin: number;
    pulse: number;
    saturation: number;
    endTidal: number;
}