import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatAutocompleteTrigger } from '@angular/material/autocomplete';

const MAX_DIGITS = 2;
const HOURS_MIN_VALUE = 0;
const HOURS_MAX_VALUE = 23;
const MINUTES_STEP = 5;
const MINUTES_MIN_VALUE = 0;
const MINUTES_MAX_VALUE = 59;

@Component({
    selector: 'app-time-picker',
    templateUrl: './time-picker.component.html',
    styleUrls: ['./time-picker.component.scss']
})
export class TimePickerComponent implements OnInit {

    @Input() timePickerData?: TimePickerData;
    @Input() submitted?: boolean;
    @Output() timeSelected: EventEmitter<TimePickerDto> = new EventEmitter<TimePickerDto>();
    
    @ViewChild('matAutocompleteHour', { read: MatAutocompleteTrigger })
    autoCompleteHours: MatAutocompleteTrigger;
    @ViewChild('matAutocompleteMinutes', { read: MatAutocompleteTrigger })
    autoCompleteMinutes: MatAutocompleteTrigger;

    timePickerForm: FormGroup<TimePickerForm>;

    hoursMinValue: number;
    hoursMaxValue: number;
    minutesStep: number;

    hoursArray: string[] = [];
    minutesArray: string[] = [];

    constructor(
		private readonly formBuilder: FormBuilder
    ) { }

    ngOnInit(): void {      
        this.initDefaultValues();
        this.initDisplayValues();

        this.timePickerForm = this.formBuilder.group({
            hour: new FormControl(''),
            minutes: new FormControl('')
        });

        this.checkIfRequired();
        this.setDefaultTimeAndEmitValue();

        window.addEventListener('scroll', this.scrollEventForHours, true);
        window.addEventListener('scroll', this.scrollEventForMinutes, true);
    }

    ngOnChanges() {
        if (this.submitted) {
            this.touchAndValidateForm();
        }
        this.setDefaultTimeAndEmitValue();
    }

    private touchAndValidateForm() {
        this.timePickerForm.controls.hour.markAsTouched();
        this.timePickerForm.controls.minutes.markAsTouched();
        this.timePickerForm.controls.hour.updateValueAndValidity();
        this.timePickerForm.controls.minutes.updateValueAndValidity();
    }

    private initDefaultValues() {
        this.hoursMinValue = this.timePickerData?.hoursMinValue || HOURS_MIN_VALUE;
        this.hoursMaxValue = this.timePickerData?.hoursMaxValue || HOURS_MAX_VALUE;
        this.minutesStep = this.timePickerData?.minuteStep || MINUTES_STEP;
    }

    private initDisplayValues() {
        this.hoursArray = this.generateStringArray(
            this.hoursMinValue,
            this.hoursMaxValue
        );
        this.minutesArray = this.generateStringArray(
            MINUTES_MIN_VALUE,
            MINUTES_MAX_VALUE,
            this.minutesStep
        );
    }

    private setDefaultTimeAndEmitValue() {
        if (this.timePickerData?.defaultTime) {
            let actualTime = this.timePickerData.defaultTime;
            let hours = actualTime.hours.toString();
            let minutes = Number(actualTime.minutes.toString());
            this.timePickerForm.setValue({ hour: this.getValueBetweenLimits(hours, this.hoursMinValue, this.hoursMaxValue), minutes: this.transformNumberToTwoDigitsString(minutes) })
            this.emitNewValue();
        }
    }

    private checkIfRequired() :void {
		if(this.timePickerData?.isRequired){
            this.timePickerForm.controls.hour.setValidators([Validators.required]);
            this.timePickerForm.controls.minutes.setValidators([Validators.required]);
		}
	}

    private scrollEventForHours = (event: any): void => {
        if(this.autoCompleteHours?.panelOpen){
            this.autoCompleteHours.updatePosition();
        }
    };

    private scrollEventForMinutes = (event: any): void => {
        if(this.autoCompleteMinutes?.panelOpen){
            this.autoCompleteMinutes.updatePosition();
        }
    };

    private emitNewValue() {
        let selectedTime: TimePickerDto = {
            hours: Number(this.timePickerForm.value.hour),
            minutes: Number(this.timePickerForm.value.minutes)
        };
        this.timeSelected.emit(selectedTime);
    }

    handleHourSelected(){
        let value = this.timePickerForm.value.hour;
        value = this.handleValue(value);
        this.handleCurrentMinutesValue();
        this.timePickerForm.controls.hour.setValue(this.getValueBetweenLimits(value, this.hoursMinValue, this.hoursMaxValue));
        this.emitNewValue();
    }

    private handleCurrentMinutesValue() {
        let currentMinutes = this.timePickerForm.value.minutes;
        if (!currentMinutes.length) {
            this.timePickerForm.controls.minutes.setValue(this.transformNumberToTwoDigitsString(MINUTES_MIN_VALUE));
        }
    }

    handleMinutesSelected(){
        let value = this.timePickerForm.value.minutes;
        value = this.handleValue(value);
        this.handleCurrentHoursValue();
        this.timePickerForm.controls.minutes.setValue(this.getValueBetweenLimits(value, MINUTES_MIN_VALUE, MINUTES_MAX_VALUE));
        this.emitNewValue();
    }

    private handleCurrentHoursValue() {
        let currentHour = this.timePickerForm.value.hour;
        if (!currentHour.length) {
            this.timePickerForm.controls.hour.setValue(this.transformNumberToTwoDigitsString(this.hoursMinValue));
        }
    }

    private getValueBetweenLimits(value: string, minValue: number, maxValue: number): string {
        let valueAsNumber = Number(value);
        valueAsNumber = valueAsNumber < minValue ? minValue : valueAsNumber;
        valueAsNumber = valueAsNumber > maxValue ? maxValue : valueAsNumber;
        return this.transformNumberToTwoDigitsString(valueAsNumber)
    }

    private handleValue(inputValue: string): string {
        let value = inputValue.trim().replace(/\D/g, '');

        if (value.length > MAX_DIGITS) {
            value = value.slice(0, MAX_DIGITS)
        }

        return value.padStart(MAX_DIGITS, '0')
    }

    private generateStringArray(minValue: number, maxValue: number, step: number = 1): string[] {
        const stringArray: string[] = [];
        for (let value = minValue; value <= maxValue; value += step) {
            stringArray.push(this.transformNumberToTwoDigitsString(value));
        }
        return stringArray
    }

    private transformNumberToTwoDigitsString(value: number): string{
        return value.toString().padStart(MAX_DIGITS, '0');
    }
}

export interface TimePickerForm {
    hour: FormControl<string>,
    minutes: FormControl<string>,
}

export interface TimePickerData {
    defaultTime?: TimePickerDto,
    hoursMinValue?: number,
    hoursMaxValue?: number,
    minuteStep?: number,
    hideLabel?: boolean,
    isRequired?: boolean,
}

export interface TimePickerDto {
    hours: number;
    minutes: number;
}