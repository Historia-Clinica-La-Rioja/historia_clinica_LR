import { Component, Inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TimeDto } from '@api-rest/api-model';
import { AnestheticReportVitalSignsService, MeasuringPointData, MeasuringPointForm } from '../../services/anesthetic-report-vital-signs.service';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { VITAL_SIGNS } from '@historia-clinica/constants/validation-constants';
import { TranslateService } from '@ngx-translate/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TimePickerData } from '@presentation/components/time-picker/time-picker.component';
import { Subject, takeUntil } from 'rxjs';

@Component({
    selector: 'app-edit-measuring-point',
    templateUrl: './edit-measuring-point.component.html',
    styleUrls: ['./edit-measuring-point.component.scss']
})
export class EditMeasuringPointComponent {

    form: FormGroup;

    bloodPressureMinError: String;
    bloodPressureMaxError: String;
    pulseError: String;
    saturationError: String;
    endTidalError: String;    
    timePickerData: TimePickerData;

    private readonly destroy$ = new Subject();

    constructor(
		@Inject(MAT_DIALOG_DATA) public data: EditPointData,
        private readonly translateService: TranslateService,
		private readonly dialog: MatDialogRef<EditMeasuringPointComponent>,
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

        this.timePickerData = {
            ...this.data.service.getTimePickerData(),
            defaultTime: this.data.measuringPoint.measuringPointStartTime
        }

        this.preloadMeasuringPoint();

        this.handleFormChanges();
    }

    ngOnDestroy() {
        this.destroy$.next(true);
        this.destroy$.unsubscribe();
    }

    setMeasuringPointStartTime(time: TimeDto) {
        this.form.controls.measuringPointStartTime.setValue(time);
    }

    setSelectedDate(date: Date) {
        this.form.controls.measuringPointStartDate.setValue(date);
    }

    preloadMeasuringPoint() {
        this.form.controls.measuringPointStartDate.setValue(this.data.measuringPoint.measuringPointStartDate);
        this.form.controls.measuringPointStartTime.setValue(this.data.measuringPoint.measuringPointStartTime);
        this.form.controls.bloodPressureMax.setValue(this.data.measuringPoint.bloodPressureMax);
        this.form.controls.bloodPressureMin.setValue(this.data.measuringPoint.bloodPressureMin);
        this.form.controls.pulse.setValue(this.data.measuringPoint.pulse);
        this.form.controls.saturation.setValue(this.data.measuringPoint.saturation);
        this.form.controls.endTidal.setValue(this.data.measuringPoint.endTidal);
    }

    saveEditMeasuringPoint() {
        this.dialog.close(this.buildMeasuringPointDto());
    }

    private buildMeasuringPointDto(): MeasuringPointData {
        return {
            measuringPointStartDate: this.form.value.measuringPointStartDate,
            measuringPointStartTime: this.form.value.measuringPointStartTime,
            bloodPressureMax: this.form.value.bloodPressureMax,
            bloodPressureMin: this.form.value.bloodPressureMin,
            pulse: this.form.value.pulse,
            saturation: this.form.value.saturation,
            endTidal: this.form.value.endTidal,
        }
    }

    private handleFormChanges() {
        this.form.controls.bloodPressureMax.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(_ => {
			this.checkBloodPressureMaxErrors();
		});

		this.form.controls.bloodPressureMin.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(_ => {
			this.checkBloodPressureMinErrors();
		});
        
		this.form.controls.pulse.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(_ => {
			this.checkPulseErrors();
		});

		this.form.controls.saturation.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(_ => {
			this.checSaturationErrors();
		});
        
		this.form.controls.endTidal.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(_ => {
			this.checkEndTidalErrors();
		});
    }

    private checkBloodPressureMinErrors() {
        if (this.form.controls.bloodPressureMin.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: VITAL_SIGNS.MIN.bloodPressure }).subscribe(
                (errorMsg: string) => this.bloodPressureMinError = errorMsg
            );
        }
        else if (this.form.controls.bloodPressureMin.hasError('max')) {
            this.translateService.get('forms.MAX_ERROR', { max: VITAL_SIGNS.MAX.bloodPressure }).subscribe(
                (errorMsg: string) => this.bloodPressureMinError = errorMsg
            );
        }
        else if (this.form.controls.bloodPressureMin.hasError('pattern')) {
            this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
                (errorMsg: string) => this.bloodPressureMinError = errorMsg
            );
        }
        else {
            this.bloodPressureMinError = '';
        }
    }

    private checkBloodPressureMaxErrors() {
        if (this.form.controls.bloodPressureMax.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: VITAL_SIGNS.MIN.bloodPressure }).subscribe(
                (errorMsg: string) => this.bloodPressureMaxError = errorMsg
            );
        }
        else if (this.form.controls.bloodPressureMax.hasError('max')) {
            this.translateService.get('forms.MAX_ERROR', { max: VITAL_SIGNS.MAX.bloodPressure }).subscribe(
                (errorMsg: string) => this.bloodPressureMaxError = errorMsg
            );
        }
        else if (this.form.controls.bloodPressureMax.hasError('pattern')) {
            this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
                (errorMsg: string) => this.bloodPressureMaxError = errorMsg
            );
        }
        else {
            this.bloodPressureMaxError = '';
        }
    }

    private checkPulseErrors() {
        if (this.form.controls.pulse.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: VITAL_SIGNS.MIN.pulse }).subscribe(
                (errorMsg: string) => this.pulseError = errorMsg
            );
        }
        else if (this.form.controls.pulse.hasError('max')) {
            this.translateService.get('forms.MAX_ERROR', { max: VITAL_SIGNS.MAX.pulse }).subscribe(
                (errorMsg: string) => this.pulseError = errorMsg
            );
        }
        else if (this.form.controls.pulse.hasError('pattern')) {
            this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
                (errorMsg: string) => this.pulseError = errorMsg
            );
        }
        else {
            this.pulseError = '';
        }
    }

    private checSaturationErrors() {
        if (this.form.controls.saturation.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: VITAL_SIGNS.MIN.saturation }).subscribe(
                (errorMsg: string) => this.saturationError = errorMsg
            );
        }
        else if (this.form.controls.saturation.hasError('max')) {
            this.translateService.get('forms.MAX_ERROR', { max: VITAL_SIGNS.MAX.saturation }).subscribe(
                (errorMsg: string) => this.saturationError = errorMsg
            );
        }
        else if (this.form.controls.saturation.hasError('pattern')) {
            this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
                (errorMsg: string) => this.saturationError = errorMsg
            );
        }
        else {
            this.saturationError = ''
        }
    }

    private checkEndTidalErrors() {
        if (this.form.controls.endTidal.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: VITAL_SIGNS.MIN.endTidal }).subscribe(
                (errorMsg: string) => this.endTidalError = errorMsg
            );
        }
        else if (this.form.controls.endTidal.hasError('max')) {
            this.translateService.get('forms.MAX_ERROR', { max: VITAL_SIGNS.MAX.endTidal }).subscribe(
                (errorMsg: string) => this.endTidalError = errorMsg
            );
        }
        else if (this.form.controls.endTidal.hasError('pattern')) {
            this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
                (errorMsg: string) => this.endTidalError = errorMsg
            );
        }
        else {
            this.endTidalError = ''
        }
    }

}

export interface EditPointData {
    service: AnestheticReportVitalSignsService;
    measuringPoint: MeasuringPointData;
}