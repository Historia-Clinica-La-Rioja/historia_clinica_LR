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
		this.handleFormControlChanges('bloodPressureMax', 'bloodPressureMaxError');
		this.handleFormControlChanges('bloodPressureMin', 'bloodPressureMinError');
		this.handleFormControlChanges('pulse', 'pulseError');
		this.handleFormControlChanges('saturation', 'saturationError');
		this.handleFormControlChanges('endTidal', 'endTidalError');
	}

	private handleFormControlChanges(controlName: string, errorProperty: string) {
		this.form.controls[controlName].valueChanges.pipe(takeUntil(this.destroy$)).subscribe(_ => {
			this.checkErrors(controlName, errorProperty);
		});
	}

	private checkErrors(controlName: string, errorProperty: string) {
		const control = this.form.controls[controlName];
		controlName = this.isBloodPresure(controlName)
		if (control.hasError('min')) {
			this.translateService.get('forms.MIN_ERROR', { min: VITAL_SIGNS.MIN[controlName] }).subscribe(
				(errorMsg: string) => { this[errorProperty] = errorMsg; }
			);
		}
		else if (control.hasError('max')) {
			this.translateService.get('forms.MAX_ERROR', { max: VITAL_SIGNS.MAX[controlName] }).subscribe(
				(errorMsg: string) => { this[errorProperty] = errorMsg; }
			);
		}
		else if (control.hasError('pattern')) {
			this.translateService.get('forms.FORMAT_NUMERIC_INTEGER').subscribe(
				(errorMsg: string) => { this[errorProperty] = errorMsg; }
			);
		}
		else {
			this[errorProperty] = '';
		}
	}

	private isBloodPresure(controlName: string): string {
		if (controlName === 'bloodPressureMax' || controlName === 'bloodPressureMin') {
			return 'bloodPressure'
		}
		return controlName
	}
}

export interface EditPointData {
    service: AnestheticReportVitalSignsService;
    measuringPoint: MeasuringPointData;
}
