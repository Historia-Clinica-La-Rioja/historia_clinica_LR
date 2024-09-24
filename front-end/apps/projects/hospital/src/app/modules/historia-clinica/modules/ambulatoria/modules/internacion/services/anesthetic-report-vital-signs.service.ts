import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { DateDto, MeasuringPointDto, ProcedureDescriptionDto, TimeDto } from '@api-rest/api-model';
import { dateToDateDto, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { getArrayCopyWithoutElementAtIndex, removeFrom } from '@core/utils/array.utils';
import { isEqualDate, sameDayMonthAndYear, sameHourAndMinute } from '@core/utils/date.utils';
import { ToFormGroup } from '@core/utils/form.utils';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { VITAL_SIGNS } from '@historia-clinica/constants/validation-constants';
import { TranslateService } from '@ngx-translate/core';
import { TimePickerData, TimePickerDto } from '@presentation/components/time-picker/time-picker.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { isAfter } from 'date-fns';
import { Subject, Observable, BehaviorSubject } from 'rxjs';

const vitalSignsData: VitalSignsDataWithTimePicker = {
    anesthesiaStartDate: null,
    anesthesiaEndDate: null,
    anesthesiaStartTime: null,
    anesthesiaEndTime: null,
    surgeryStartDate: null,
    surgeryEndDate: null,
    surgeryStartTime: null,
    surgeryEndTime: null,
    anesthesiaStartTimePicker: {
        hideLabel: true,
    },
    anesthesiaEndTimePicker: {
        hideLabel: true,
    },
    surgeryStartTimePicker: {
        hideLabel: true,
    },
    surgeryEndTimePicker: {
        hideLabel: true,
    },
};

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportVitalSignsService {

    private form: FormGroup<MeasuringPointForm>;
    private vitalSignsForm: FormGroup<VitalSignsForm>;
    private measuringPoints: MeasuringPointData[] = [];

    private measuringPointsSource = new BehaviorSubject<MeasuringPointData[]>([]);
	private _measuringPoints$ = this.measuringPointsSource.asObservable();

    private isEmptySource = new BehaviorSubject<boolean>(true);
	private isEmpty$ = this.isEmptySource.asObservable();

    private vitalSignsIsEmptySource = new BehaviorSubject<boolean>(true);
	private vitalSignsIsEmpty$ = this.vitalSignsIsEmptySource.asObservable();

    private isSectionEmptySource = new BehaviorSubject<boolean>(true);
	isSectionEmpty$ = this.isSectionEmptySource.asObservable();

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

	private vitalSignsSubject: BehaviorSubject<VitalSignsDataWithTimePicker> = new BehaviorSubject<VitalSignsDataWithTimePicker>(vitalSignsData);
    vitalSigns$ = this.vitalSignsSubject.asObservable();

    private timePickerData: TimePickerData = {
        hideLabel: true,
        isRequired: true,
    }

	anesthesiaStartDate: Date | null = null;
	anesthesiaEndDate: Date | null = null;
	surgeryStartDate: Date | null = null;
	surgeryEndDate: Date | null = null;

    constructor(
		private readonly translateService: TranslateService,
        private readonly snackBarService: SnackBarService,
    ) {
        this.form = new FormGroup<MeasuringPointForm>({
            measuringPointStartDate: new FormControl(null, Validators.required),
            measuringPointStartTime: new FormControl(null, Validators.required),
            bloodPressureMax: new FormControl(null, [Validators.min(VITAL_SIGNS.MIN.bloodPressure), Validators.max(VITAL_SIGNS.MAX.bloodPressure), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
            bloodPressureMin: new FormControl(null, [Validators.min(VITAL_SIGNS.MIN.bloodPressure), Validators.max(VITAL_SIGNS.MAX.bloodPressure), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
            pulse: new FormControl(null, [Validators.min(VITAL_SIGNS.MIN.pulse), Validators.max(VITAL_SIGNS.MAX.pulse), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
            saturation: new FormControl(null, [Validators.min(VITAL_SIGNS.MIN.saturation), Validators.max(VITAL_SIGNS.MAX.saturation), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
            endTidal: new FormControl(null, [Validators.min(VITAL_SIGNS.MIN.endTidal), Validators.max(VITAL_SIGNS.MAX.endTidal), Validators.pattern(PATTERN_INTEGER_NUMBER)]),
        });

        this.vitalSignsForm = new FormGroup<VitalSignsForm>({
            anesthesiaStartDate: new FormControl(null),
            anesthesiaEndDate: new FormControl(null),
            anesthesiaStartTime: new FormControl(null),
            anesthesiaEndTime: new FormControl(null),
            surgeryStartDate: new FormControl(null),
            surgeryEndDate: new FormControl(null),
            surgeryStartTime: new FormControl(null),
            surgeryEndTime: new FormControl(null),
        })

        this.handleFormChanges();
        this.checkSectionEmptyness();

    }

    private checkSectionEmptyness() {
        this.isEmpty$.subscribe(isEmpty => {
            this.isSectionEmptySource.next(isEmpty);
        });
        this.vitalSignsIsEmpty$.subscribe(isEmpty => {
            this.isSectionEmptySource.next(isEmpty);
        })
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
		this.handleFormControlChanges('bloodPressureMax', 'bloodPressureMaxSource');
		this.handleFormControlChanges('bloodPressureMin', 'bloodPressureMinSource');
		this.handleFormControlChanges('pulse', 'pulseSource');
		this.handleFormControlChanges('saturation', 'saturationSource');
		this.handleFormControlChanges('endTidal', 'endTidalSource');
	}

	private handleFormControlChanges(controlName: string, errorSource: string) {
		this.form.controls[controlName].valueChanges.subscribe(_ => {
			this.checkErrors(controlName, errorSource);
		});
	}

	private checkErrors(controlName: string, errorSource: string) {
		const control = this.form.controls[controlName];
		controlName = this.isBloodPresure(controlName)
		if (control.hasError('min')) {
			this.translateService.get('forms.MIN_ERROR', { min: VITAL_SIGNS.MIN[controlName] }).subscribe(
				(errorMsg: string) => { this[errorSource].next(errorMsg); }
			);
		}
		else if (control.hasError('max')) {
			this.translateService.get('forms.MAX_ERROR', { max: VITAL_SIGNS.MAX[controlName] }).subscribe(
				(errorMsg: string) => { this[errorSource].next(errorMsg); }
			);
		}
		else if (control.hasError('pattern')) {
			this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
				(errorMsg: string) => { this[errorSource].next(errorMsg); }
			);
		}
		else {
			this[errorSource].next();
		}
	}

	private isBloodPresure(controlName: string): string {
		if (controlName === 'bloodPressureMax' || controlName === 'bloodPressureMin') {
			return 'bloodPressure'
		}
		return controlName
	}

    setStartingDate(date: Date) {
        this.form.controls.measuringPointStartDate.setValue(date);
    }

    handleMeasuringPointRegister() {
        if (this.form.valid){
            this.validateAndAddMeasuringPoint();
            this.clearForm();
        }
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

    getVitalSignsForm(): FormGroup {
        return this.vitalSignsForm;
    }

    getVitalSignsData(): VitalSignsData {
        return {
            ...this.vitalSignsForm.value
        }
    }

    setFormDateAttributeValue(attribute: VitalSignsAttribute, date: Date) {
        this.vitalSignsForm.get(attribute).setValue(date);
        this.vitalSignsIsEmptySource.next(false);
    }

    setFormTimeAttributeValue(attribute: VitalSignsAttribute, time: TimeDto) {
        this.vitalSignsForm.get(attribute).setValue(time);
        this.vitalSignsIsEmptySource.next(false);
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

	setMeasuringPoints(data: MeasuringPointDto[]) {
        data.forEach(point => {
            let pointData: MeasuringPointData = {
                measuringPointStartDate: this.convertToDate(point.date),
                measuringPointStartTime: point.time,
                bloodPressureMax: point.bloodPressureMax,
                bloodPressureMin: point.bloodPressureMin,
                pulse: point.bloodPulse,
                saturation: point.o2Saturation,
                endTidal: point.co2EndTidal
            };
            this.addAndEmitUpdatedValue({...pointData });
            this.clearForm();
        });
    }

	setDataVitalSigns(vitalSigns: ProcedureDescriptionDto): void {
		if (vitalSigns) {
			this.setVitalSignsToForm(vitalSigns);
			this.setAndEmitVitalSigns(vitalSigns);
		} else {
			this.vitalSignsSubject.next(this.getVitalSignsData());
			this.resetTime();
		}
	}

	private setVitalSignsToForm(vitalSigns: ProcedureDescriptionDto) {
		if (vitalSigns.anesthesiaStartDate) {
			this.setFormDateAttributeValue('anesthesiaStartDate', this.convertToDate(vitalSigns.anesthesiaStartDate));
		}
		if (vitalSigns.anesthesiaEndDate) {
			this.setFormDateAttributeValue('anesthesiaEndDate', this.convertToDate(vitalSigns.anesthesiaEndDate));
		}
		if (vitalSigns.anesthesiaStartTime) {
			this.setFormTimeAttributeValue('anesthesiaStartTime', vitalSigns.anesthesiaStartTime);
		}
		if (vitalSigns.anesthesiaEndTime) {
			this.setFormTimeAttributeValue('anesthesiaEndTime', vitalSigns.anesthesiaEndTime);
		}
		if (vitalSigns.surgeryStartDate) {
			this.setFormDateAttributeValue('surgeryStartDate', this.convertToDate(vitalSigns.surgeryStartDate));
		}
		if (vitalSigns.surgeryEndDate) {
			this.setFormDateAttributeValue('surgeryEndDate', this.convertToDate(vitalSigns.surgeryEndDate));
		}
		if (vitalSigns.surgeryStartTime) {
			this.setFormTimeAttributeValue('surgeryStartTime', vitalSigns.surgeryStartTime);
		}
		if (vitalSigns.surgeryEndTime) {
			this.setFormTimeAttributeValue('surgeryEndTime', vitalSigns.surgeryEndTime);
		}
	}

	private setAndEmitVitalSigns(vitalSigns: ProcedureDescriptionDto) {
        let updatedVitalSigns: VitalSignsDataWithTimePicker = { ...this.vitalSignsSubject.getValue() };
		updatedVitalSigns = this.mapToDate(vitalSigns, updatedVitalSigns);
		updatedVitalSigns = this.mapToTime(vitalSigns, updatedVitalSigns);
        this.vitalSignsSubject.next(updatedVitalSigns);
	}

	private mapToDate(data: ProcedureDescriptionDto, updatedVitalSigns: VitalSignsDataWithTimePicker) {
		if (data.anesthesiaStartDate)
			updatedVitalSigns.anesthesiaStartDate = this.convertToDate(data.anesthesiaStartDate);

		if (data.anesthesiaEndDate)
			updatedVitalSigns.anesthesiaEndDate = this.convertToDate(data.anesthesiaEndDate);

		if (data.surgeryStartDate)
			updatedVitalSigns.surgeryStartDate = this.convertToDate(data.surgeryStartDate);

		if (data.surgeryEndDate)
			updatedVitalSigns.surgeryEndDate = this.convertToDate(data.surgeryEndDate);

		return updatedVitalSigns;
	}

	private mapToTime(data: ProcedureDescriptionDto, updatedVitalSigns: VitalSignsDataWithTimePicker) {
        updatedVitalSigns.anesthesiaStartTime = data?.anesthesiaStartTime;
		updatedVitalSigns.anesthesiaStartTimePicker = data?.anesthesiaStartTime
			? { defaultTime: this.convertToTimePickerDto(data.anesthesiaStartTime), hideLabel: true }
			: { hideLabel: true };

        updatedVitalSigns.anesthesiaEndTime = data?.anesthesiaEndTime;
		updatedVitalSigns.anesthesiaEndTimePicker = data?.anesthesiaEndTime
			? { defaultTime: this.convertToTimePickerDto(data.anesthesiaEndTime), hideLabel: true }
			: { hideLabel: true };

        updatedVitalSigns.surgeryStartTime = data?.surgeryStartTime;
		updatedVitalSigns.surgeryStartTimePicker = data?.surgeryStartTime
			? { defaultTime: this.convertToTimePickerDto(data.surgeryStartTime), hideLabel: true }
			: { hideLabel: true };

        updatedVitalSigns.surgeryEndTime = data?.surgeryEndTime;
		updatedVitalSigns.surgeryEndTimePicker = data?.surgeryEndTime
			? { defaultTime: this.convertToTimePickerDto(data.surgeryEndTime), hideLabel: true }
			: { hideLabel: true };

        return updatedVitalSigns;
	}

    private resetTime() {
        let updatedVitalSigns: VitalSignsDataWithTimePicker = { ...this.vitalSignsSubject.getValue() };
        updatedVitalSigns.anesthesiaStartTime = null;
        updatedVitalSigns.anesthesiaStartTimePicker = { hideLabel: true };
        updatedVitalSigns.anesthesiaEndTime = null;
        updatedVitalSigns.anesthesiaEndTimePicker = { hideLabel: true };
        updatedVitalSigns.surgeryStartTime = null;
        updatedVitalSigns.surgeryStartTimePicker = { hideLabel: true };
        updatedVitalSigns.surgeryEndTime = null;
        updatedVitalSigns.surgeryEndTimePicker = { hideLabel: true };
        this.vitalSignsSubject.next(updatedVitalSigns);
    }

	convertToDate(dateDto: DateDto): Date {
		return new Date(dateDto.year, dateDto.month - 1, dateDto.day);
	}

	private convertToTimePickerDto(timeDto: TimeDto): TimePickerDto {
        return {
            hours: timeDto.hours,
            minutes: timeDto.minutes
        };
    }

	validateDates(vitalSignsForm: FormGroup<ToFormGroup<VitalSignsData>>): void {
		this.validateDate(vitalSignsForm, 'anesthesiaStartDate', 'anesthesiaEndDate', 'anesthesiaStartTime', 'anesthesiaEndTime');
		this.validateDate(vitalSignsForm, 'surgeryStartDate', 'surgeryEndDate', 'surgeryStartTime', 'surgeryEndTime');
	}

	private validateDate(vitalSignsForm: FormGroup<ToFormGroup<VitalSignsData>>, startDateKey: string, endDateKey: string, startTimeKey: string, endTimeKey: string): void {
		const startDate = vitalSignsForm.controls[startDateKey].value;
		const endDate = vitalSignsForm.controls[endDateKey].value;
		const startTime = vitalSignsForm.controls[startTimeKey].value;
		const endTime = vitalSignsForm.controls[endTimeKey].value;

		const startTimeDate = startTime ? timeDtoToDate(startTime) : null;
		const endTimeDate = endTime ? timeDtoToDate(endTime) : null;

		this[startDateKey] = startDate ? new Date(startDate) : null;
		this[endDateKey] = endDate ? new Date(endDate) : null;

		if ((this[startDateKey] && this[endDateKey]) && sameDayMonthAndYear(this[startDateKey], this[endDateKey])) {
			if (!(startTime && endTime) || sameHourAndMinute(startTimeDate, endTimeDate) || isAfter(startTimeDate, endTimeDate)) {
				vitalSignsForm.controls[endDateKey].setErrors({ endTimeBeforeStartTime: true });
			} else {
				vitalSignsForm.controls[endDateKey].setErrors(null);
			}
		} else {
			vitalSignsForm.controls[endDateKey].setErrors(null);
		}
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

export type VitalSignsAttribute = keyof VitalSignsData;

export interface VitalSignsForm {
    anesthesiaStartDate?: FormControl<Date>;
    anesthesiaEndDate?: FormControl<Date>;
    anesthesiaStartTime?: FormControl<TimeDto>;
    anesthesiaEndTime?: FormControl<TimeDto>;
    surgeryStartDate?: FormControl<Date>;
    surgeryEndDate?: FormControl<Date>;
    surgeryStartTime?: FormControl<TimeDto>;
    surgeryEndTime?: FormControl<TimeDto>;
}
export interface VitalSignsData {
    anesthesiaStartDate?: Date;
    anesthesiaEndDate?: Date;
    anesthesiaStartTime?: TimeDto;
    anesthesiaEndTime?: TimeDto;
    surgeryStartDate?: Date;
    surgeryEndDate?: Date;
    surgeryStartTime?: TimeDto;
    surgeryEndTime?: TimeDto;
}

export interface VitalSignsDataWithTimePicker extends VitalSignsData {
    anesthesiaStartTimePicker?: TimePickerData;
    anesthesiaEndTimePicker?: TimePickerData;
    surgeryStartTimePicker?: TimePickerData;
    surgeryEndTimePicker?: TimePickerData;
}
