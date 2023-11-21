import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { hasError } from '@core/utils/form.utils';
import { PREGNANTPEOPLE_REPORT_TYPES } from './constants/pregnantpeople-report-types';
import { MIN_DATE } from '@core/utils/date.utils';
import { PregnantpeopleReportsService } from '@api-rest/services/pregnantpeople-reports.service';
import { Moment } from 'moment';
import { dateToMoment, newMoment } from '@core/utils/moment.utils';

@Component({
  selector: 'app-reportes-personagestante',
  templateUrl: './reportes-personagestante.component.html',
  styleUrls: ['./reportes-personagestante.component.scss']
})
export class ReportesPersonagestanteComponent implements OnInit {

  form: UntypedFormGroup;
  public submitted = false;

  public hasError = hasError;

  REPORT_TYPES = PREGNANTPEOPLE_REPORT_TYPES;

  minDate = MIN_DATE

  constructor(
    private readonly formBuilder: UntypedFormBuilder,
    private readonly pregnantpeopleReportsService: PregnantpeopleReportsService,
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      reportType: [null, Validators.required],
      startDate: [this.firstDayOfThisMonth(), Validators.required],
      endDate: [this.lastDayOfThisMonth(), Validators.required],
    });
  }

  private firstDayOfThisMonth(): Moment {
    const today = newMoment();
    return dateToMoment(new Date(today.year(), today.month(), 1));
  }

  private lastDayOfThisMonth(): Moment {
    const today = newMoment();
    return dateToMoment(new Date(today.year(), today.month() + 1, 0));
  }

  maxStartDate(endDate) {
    const today = newMoment();
    if (endDate) {
      return (today.isBefore(endDate)) ? today : endDate;
    }
    return today;
  }

  checkValidDates() {
    if (this.form.value.startDate && this.form.value.endDate) {
      const endDate: Moment = this.form.value.endDate;
      if (endDate.isBefore(this.form.value.startDate)) {
        this.form.controls.endDate.setErrors({ min: true });
        this.form.controls.startDate.setErrors({ max: true });
      } else {
        this.form.controls.endDate.setErrors(null);
        this.checkStartDateIsSameOrBeforeToday();
      }
    } else if (this.form.value.startDate) {
      this.checkStartDateIsSameOrBeforeToday();
    } else if (this.form.value.endDate) {
      this.form.controls.endDate.setErrors(null);
    }
  }

  private checkStartDateIsSameOrBeforeToday() {
    const today = newMoment();
    (today.isSameOrAfter(this.form.value.startDate))
      ? this.form.controls.startDate.setErrors(null)
      : this.form.controls.startDate.setErrors({ afterToday: true });
  }

  generatePregnantpeopleReport() {
    this.submitted = true;
    if(this.form.valid) {
      const params = {
        startDate: this.form.controls.startDate.value,
        endDate: this.form.controls.endDate.value
      }
      const pregnantpeopleReportId = this.form.controls.reportType.value;
      switch (pregnantpeopleReportId) {
        case 1:
          this.pregnantpeopleReportsService.getPregnantAttentionsReport(params, `${this.REPORT_TYPES[0].description}.xls`).subscribe();
          break;
        case 2:
          this.pregnantpeopleReportsService.getPregnantControlsReport(params, `${this.REPORT_TYPES[1].description}.xls`).subscribe();
          break;
        default:
      }
    }
  }

}
