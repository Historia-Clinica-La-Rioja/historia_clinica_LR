import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { hasError } from '@core/utils/form.utils';
import { GENERAL_REPORT_TYPES } from './constants/general-report-types';
import { MIN_DATE } from '@core/utils/date.utils';
import { GeneralReportsService } from '@api-rest/services/general-reports.service';
import { Moment } from 'moment';
import { dateToMoment, newMoment } from '@core/utils/moment.utils';

@Component({
  selector: 'app-reportes-generales',
  templateUrl: './reportes-generales.component.html',
  styleUrls: ['./reportes-generales.component.scss']
})
export class ReportesGeneralesComponent implements OnInit {

  form: UntypedFormGroup;
  public submitted = false;

  public hasError = hasError;

  REPORT_TYPES = GENERAL_REPORT_TYPES;

  minDate = MIN_DATE;

  constructor(
    private readonly formBuilder: UntypedFormBuilder,
    private readonly generalReportsService: GeneralReportsService,
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

  generateGeneralReport() {
    this.submitted = true;
    if (this.form.valid) {
      const params = {
        startDate: this.form.controls.startDate.value,
        endDate: this.form.controls.endDate.value
      }
      const generalReportId = this.form.controls.reportType.value;
      switch (generalReportId) {
        case 1:
          this.generalReportsService.getEmergencyReport(params, `${this.REPORT_TYPES[0].description}.xls`).subscribe();
          break;
        case 2:
          this.generalReportsService.getDiabeticReport(params, `${this.REPORT_TYPES[1].description}.xls`).subscribe();
          break;
        case 3:
          this.generalReportsService.getHypertensiveReport(params, `${this.REPORT_TYPES[2].description}.xls`).subscribe();
          break;
        case 4:
          this.generalReportsService.getComplementaryStudiesReport(params, `${this.REPORT_TYPES[3].description}.xls`).subscribe();
          break;
          case 5:
          this.generalReportsService.getMedicationPrescriptionReport(params, `${this.REPORT_TYPES[4].description}.xls`).subscribe();
          break;
        default:
      }
    }
  }

}
