import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MeasuringPointDto, TimeDto } from '@api-rest/api-model';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { getArrayCopyWithoutElementAtIndex, removeFrom } from '@core/utils/array.utils';
import { isEqualDate } from '@core/utils/date.utils';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { VITAL_SIGNS } from '@historia-clinica/constants/validation-constants';
import { TranslateService } from '@ngx-translate/core';
import { TimePickerData } from '@presentation/components/time-picker/time-picker.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Subject, Observable, BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportVitalSignsService {

    private form: FormGroup<MeasuringPointForm>;
    private measuringPoints: MeasuringPointData[] = [];
    
    private measuringPointsSource = new BehaviorSubject<MeasuringPointData[]>([]);
	private _measuringPoints$ = this.measuringPointsSource.asObservable();
    
    private isEmptySource = new BehaviorSubject<boolean>(true);
	isEmpty$ = this.isEmptySource.asObservable();

    private bloodPressureMaxSource = new Subject<string | void>();
	private _bloodPressureMax$ = this.bloodPressureMaxSource.asObservable();
    private bloodPressureMinSource = new Subject<string | void>();
	private _bloodPressureMin$ = this.bloodPressureMinSource.asObservable();
    private pulseSource = new Subject<string | void>();
	private _pulse$ = this.pulseSource.asObservable();
    private saturationSource = new Subject<string | void>();
	private _saturation$ = this.saturationSource.asObservable();
    private endTidalSource = new Subject<string | void>();
	private _endTidal$ = this.endTidalSource.asObservable();

    private timePickerData: TimePickerData = {
        hideLabel: true,
    }

    constructor(
		private readonly translateService: TranslateService,
        private readonly snackBarService: SnackBarService,
    ) {
        this.form = new FormGroup<MeasuringPointForm>({
            measuringPointStartDate: new FormControl(null),
            measuringPointStartTime: new FormControl(null),
            bloodPressureMax: new FormControl(null, [Validators.min(VITAL_SIGNS.MIN.bloodPressure), Validators.max(VITAL_SIGNS.MAX.bloodPressure), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
            bloodPressureMin: new FormControl(null, [Validators.min(VITAL_SIGNS.MIN.bloodPressure), Validators.max(VITAL_SIGNS.MAX.bloodPressure), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
            pulse: new FormControl(null, [Validators.min(VITAL_SIGNS.MIN.pulse), Validators.max(VITAL_SIGNS.MAX.pulse), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
            saturation: new FormControl(null, [Validators.min(VITAL_SIGNS.MIN.saturation), Validators.max(VITAL_SIGNS.MAX.saturation), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
            endTidal: new FormControl(null, [Validators.min(VITAL_SIGNS.MIN.endTidal), Validators.max(VITAL_SIGNS.MAX.endTidal), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
        });

        this.handleFormChanges();
    }

    getTimePickerData(): TimePickerData {
        return this.timePickerData;
    }

    getMeasuringPointsAsMeasuringPointDto(): MeasuringPointDto[] {
        return this.measuringPoints.map(mp => {
            return {
                bloodPressureMax: mp.bloodPressureMax,
                bloodPressureMin: mp.bloodPressureMin,
                bloodPulse: mp.pulse,
                co2EndTidal: mp.endTidal,
                date: dateToDateDto(mp.measuringPointStartDate),
                o2Saturation: mp.saturation,
                time: mp.measuringPointStartTime,
            }}
        )
    }

    get measuringPoints$(): Observable<MeasuringPointData[]> {
		return this._measuringPoints$;
	}
    
    get bloodPressureMaxError$(): Observable<string | void> {
		return this._bloodPressureMax$;
	}

    get bloodPressureMinError$(): Observable<string | void> {
		return this._bloodPressureMin$;
	}
    
    get pulseError$(): Observable<string | void> {
		return this._pulse$;
	}

    get saturationError$(): Observable<string | void> {
		return this._saturation$;
	}

    get endTidalError$(): Observable<string | void> {
		return this._endTidal$;
	}

    private handleFormChanges() {
        this.form.controls.bloodPressureMax.valueChanges.subscribe(_ => {
			this.checkBloodPressureMaxErrors();
		});

		this.form.controls.bloodPressureMin.valueChanges.subscribe(_ => {
			this.checkBloodPressureMinErrors();
		});
        
		this.form.controls.pulse.valueChanges.subscribe(_ => {
			this.checkPulseErrors();
		});

		this.form.controls.saturation.valueChanges.subscribe(_ => {
			this.checSaturationErrors();
		});
        
		this.form.controls.endTidal.valueChanges.subscribe(_ => {
			this.checkEndTidalErrors();
		});
    }

    private checkBloodPressureMinErrors() {
        if (this.form.controls.bloodPressureMin.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: VITAL_SIGNS.MIN.bloodPressure }).subscribe(
                (errorMsg: string) => this.bloodPressureMinSource.next(errorMsg)
            );
        }
        else if (this.form.controls.bloodPressureMin.hasError('max')) {
            this.translateService.get('forms.MAX_ERROR', { max: VITAL_SIGNS.MAX.bloodPressure }).subscribe(
                (errorMsg: string) => this.bloodPressureMinSource.next(errorMsg)
            );
        }
        else if (this.form.controls.bloodPressureMin.hasError('pattern')) {
            this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
                (errorMsg: string) => this.bloodPressureMinSource.next(errorMsg)
            );
        }
        else {
            this.bloodPressureMinSource.next();
        }
    }

    private checkBloodPressureMaxErrors() {
        if (this.form.controls.bloodPressureMax.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: VITAL_SIGNS.MIN.bloodPressure }).subscribe(
                (errorMsg: string) => this.bloodPressureMaxSource.next(errorMsg)
            );
        }
        else if (this.form.controls.bloodPressureMax.hasError('max')) {
            this.translateService.get('forms.MAX_ERROR', { max: VITAL_SIGNS.MAX.bloodPressure }).subscribe(
                (errorMsg: string) => this.bloodPressureMaxSource.next(errorMsg)
            );
        }
        else if (this.form.controls.bloodPressureMax.hasError('pattern')) {
            this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
                (errorMsg: string) => this.bloodPressureMaxSource.next(errorMsg)
            );
        }
        else {
            this.bloodPressureMaxSource.next();
        }
    }

    private checkPulseErrors() {
        if (this.form.controls.pulse.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: VITAL_SIGNS.MIN.pulse }).subscribe(
                (errorMsg: string) => this.pulseSource.next(errorMsg)
            );
        }
        else if (this.form.controls.pulse.hasError('max')) {
            this.translateService.get('forms.MAX_ERROR', { max: VITAL_SIGNS.MAX.pulse }).subscribe(
                (errorMsg: string) => this.pulseSource.next(errorMsg)
            );
        }
        else if (this.form.controls.pulse.hasError('pattern')) {
            this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
                (errorMsg: string) => this.pulseSource.next(errorMsg)
            );
        }
        else {
            this.pulseSource.next();
        }
    }

    private checSaturationErrors() {
        if (this.form.controls.saturation.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: VITAL_SIGNS.MIN.saturation }).subscribe(
                (errorMsg: string) => this.saturationSource.next(errorMsg)
            );
        }
        else if (this.form.controls.saturation.hasError('max')) {
            this.translateService.get('forms.MAX_ERROR', { max: VITAL_SIGNS.MAX.saturation }).subscribe(
                (errorMsg: string) => this.saturationSource.next(errorMsg)
            );
        }
        else if (this.form.controls.saturation.hasError('pattern')) {
            this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
                (errorMsg: string) => this.saturationSource.next(errorMsg)
            );
        }
        else {
            this.saturationSource.next();
        }
    }

    private checkEndTidalErrors() {
        if (this.form.controls.endTidal.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: VITAL_SIGNS.MIN.endTidal }).subscribe(
                (errorMsg: string) => this.endTidalSource.next(errorMsg)
            );
        }
        else if (this.form.controls.endTidal.hasError('max')) {
            this.translateService.get('forms.MAX_ERROR', { max: VITAL_SIGNS.MAX.endTidal }).subscribe(
                (errorMsg: string) => this.endTidalSource.next(errorMsg)
            );
        }
        else if (this.form.controls.endTidal.hasError('pattern')) {
            this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
                (errorMsg: string) => this.endTidalSource.next(errorMsg)
            );
        }
        else {
            this.endTidalSource.next();
        }
    }

    setStartingDate(date: Date) {
        this.form.controls.measuringPointStartDate.setValue(date);
    }

    handleMeasuringPointRegister() {
        this.validateAndAddMeasuringPoint();
        this.clearForm();
    }

    remove(index: number) : void {
		this.measuringPoints = removeFrom<MeasuringPointData>(this.measuringPoints, index);
		this.measuringPointsSource.next(this.measuringPoints);
        this.isEmptySource.next(!this.measuringPoints.length);
	}

    private validateAndAddMeasuringPoint() {
        let newMeasuringPoint = {
            measuringPointStartDate: this.form.value.measuringPointStartDate,
            measuringPointStartTime: this.form.value.measuringPointStartTime,
            bloodPressureMax: this.form.value.bloodPressureMax,
            bloodPressureMin: this.form.value.bloodPressureMin,
            pulse: this.form.value.pulse,
            saturation: this.form.value.saturation,
            endTidal: this.form.value.endTidal,
        }
        if (!this.itsDuplicated(this.measuringPoints, newMeasuringPoint)) {
            this.addAndEmitUpdatedValue(newMeasuringPoint);
            this.snackBarService.showSuccess('internaciones.anesthesic-report.vital-signs.SUCCESS_ADD');
        } else {
            this.snackBarService.showError('internaciones.anesthesic-report.vital-signs.ERROR_ADD');
        }
    }

    private addAndEmitUpdatedValue(measuringPoint: MeasuringPointData){
        this.measuringPoints.push(measuringPoint);
        this.measuringPointsSource.next(this.measuringPoints);
        this.isEmptySource.next(!this.measuringPoints.length);
    }

    private itsDuplicated(dataArray: MeasuringPointData[], measuringPoint: MeasuringPointData): boolean {
        return !!(dataArray.filter(mp => (isEqualDate(mp.measuringPointStartDate, measuringPoint.measuringPointStartDate) && this.isEqualTime(mp.measuringPointStartTime,measuringPoint.measuringPointStartTime))).length)
    }

    private isEqualTime(t1: TimeDto, t2: TimeDto): boolean {
        return t1.hours == t2.hours && t1.minutes == t2.minutes
    }

    private clearForm() {
        this.form.controls.bloodPressureMax.setValue(null);
        this.form.controls.bloodPressureMin.setValue(null);
        this.form.controls.pulse.setValue(null);
        this.form.controls.saturation.setValue(null);
        this.form.controls.endTidal.setValue(null);
    }

    editMeasuringPoint(newMeasuringPoint: MeasuringPointData, index: number){
        const arrayCopyWithoutEditingElement = getArrayCopyWithoutElementAtIndex(this.measuringPoints, index)
        if (!this.itsDuplicated(arrayCopyWithoutEditingElement, newMeasuringPoint)) {
            this.measuringPoints.splice(index, 1, newMeasuringPoint);
            this.measuringPointsSource.next(this.measuringPoints);
            this.snackBarService.showSuccess('internaciones.anesthesic-report.vital-signs.SUCCESS_EDIT');
        } else {
            this.snackBarService.showError('internaciones.anesthesic-report.vital-signs.ERROR_ADD');
        }
    }

    getForm(): FormGroup {
        return this.form;
    }

    setMeasuringPointData(data: MeasuringPointData) {
        data.measuringPointStartDate ? this.form.controls.measuringPointStartDate.setValue(data.measuringPointStartDate) : null ;
        data.measuringPointStartTime ? this.form.controls.measuringPointStartTime.setValue(data.measuringPointStartTime) : null ;
        data.bloodPressureMax ? this.form.controls.bloodPressureMax.setValue(data.bloodPressureMax) : null ;
        data.bloodPressureMin ? this.form.controls.bloodPressureMin.setValue(data.bloodPressureMin) : null ;
        data.pulse ? this.form.controls.pulse.setValue(data.pulse) : null ;
        data.saturation ? this.form.controls.saturation.setValue(data.saturation) : null ;
        data.endTidal ? this.form.controls.endTidal.setValue(data.endTidal) : null ;
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