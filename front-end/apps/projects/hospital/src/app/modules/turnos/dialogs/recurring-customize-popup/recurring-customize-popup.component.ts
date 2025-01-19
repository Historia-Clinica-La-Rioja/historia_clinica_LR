import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CustomRecurringAppointmentDto, WeekDayDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { NUMBER_PATTERN } from '@core/utils/form.utils';
import { RecurringCancelPopupComponent } from '../recurring-cancel-popup/recurring-cancel-popup.component';

@Component({
  selector: 'app-recurring-customize-popup',
  templateUrl: './recurring-customize-popup.component.html',
  styleUrls: ['./recurring-customize-popup.component.scss']
})

export class RecurringCustomizePopupComponent implements OnInit {

  DAYS_PER_WEEK: number = 7;
  form: FormGroup;
  selectedDay: WeekDayDto;
  days: WeekDayDto[] = []
  minDate: Date = new Date();
  dateToSetInDatepicker: Date = new Date();
  hideInputs: boolean = false;
  disableRepeatOn: boolean = false;
  options = [
    {
      value: 'afterFewWeeks',
      disabled : false
    },
    {
      value: 'withEndDate',
      disabled: true,
    },
    {
      value: 'withoutEndDate',
      disabled: true,
    },
  ];

  constructor(private readonly formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data: {appointmentDate: Date, customAppointment: CustomRecurringAppointmentDto},
              public dialogRef: MatDialogRef<RecurringCancelPopupComponent>,
              private readonly appointmentService: AppointmentsService) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      repeatEvery: [null, [Validators.required, Validators.pattern(NUMBER_PATTERN)]],
			repeatOn: [null, [Validators.required]],
      selectedOption: [this.options[0].value],
      endDate: [null],
      afterFewWeeks: [null, [Validators.required, Validators.pattern(NUMBER_PATTERN)]]
		});
    
    this.changeSelectedOption();
    this.setWeekDay();
  }

  setCustomAppointment() {
    if (this.data.customAppointment?.endDate != null) {
      this.form.get('selectedOption').setValue(this.options[1].value);
      this.changeSelectedOption();
      this.setWithEndDate(this.data.customAppointment.endDate);
      this.dateToSetInDatepicker = this.data.customAppointment.endDate
      this.form.get('repeatEvery').disable();
      this.hideInputs = true;
    }
    this.form.get('repeatEvery').setValue(this.data.customAppointment?.repeatEvery);
    const newDay: WeekDayDto = this.days.find(day => day.id === new Date(this.data.appointmentDate).getDay());
    this.selectedDay = newDay;
    this.form.get('repeatOn').setValue(this.selectedDay);
    this.disableRepeatOn = true; 
  }

  setWeekDay() {
    this.appointmentService.getWeekDay()
      .subscribe(resp => {
        if ( ! resp) return;

        this.days = resp;
        this.setCustomAppointment();
      });
  }
  
  changeSelectedOption() {
    this.form.get('selectedOption').valueChanges.subscribe(value => {
      this.options.map(v => {
        return (v.value === value) ? v.disabled = false : v.disabled = true;
      });
      this.form.get('afterFewWeeks')[ ! this.options[0].disabled ? 'enable' : 'disable']();
    });
  }

  setWithEndDate(endDate: Date) {
    this.form.get('endDate').setValue(endDate);
  }

  setWithoutEndDate() {
    this.form.get('endDate').setValue(null);
  }

  setAfterFewWeeks(event: any) {
    const weeks = event.target.value;
    let date = new Date(this.data.appointmentDate);
    date.setDate(date.getDate() + (weeks * this.DAYS_PER_WEEK));
    this.form.get('endDate').setValue(date);
  }

  setInvalidForm() {
    this.form.get('endDate').setErrors({});
    this.form.get('afterFewWeeks').setValue(null);
  }

  save() {
    this.dialogRef.close(this.setCustomRecurringAppointmentDto());
  }

  close() {
    this.dialogRef.close(
      (this.form.get("repeatEvery").enabled)
        ? undefined
        : this.setCustomRecurringAppointmentDto()
    );
  }

  setCustomRecurringAppointmentDto(): CustomRecurringAppointmentDto {
    let data: CustomRecurringAppointmentDto = {
      repeatEvery: this.form.get('repeatEvery').value,
      weekDayId: this.form.get('repeatOn').value?.id,
      endDate: this.form.get('endDate').value,
    };
        
    return data;
  }

  setSelectedDay(newDay: WeekDayDto) {
    if (this.disableRepeatOn) return;

    this.selectedDay = newDay;
    this.form.get('repeatOn').setValue(this.selectedDay);
  }
}
