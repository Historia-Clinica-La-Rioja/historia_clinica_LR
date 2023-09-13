import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { hasError } from '@core/utils/form.utils';
import { OLDERADULT_REPORT_TYPES } from './constants/olderadult-report-types';
import { MIN_DATE } from '@core/utils/date.utils';
import { OlderadultReportsService } from '@api-rest/services/olderadult-reports.service';
import { Moment } from 'moment';
import { dateToMoment, newMoment } from '@core/utils/moment.utils';

@Component({
  selector: 'app-reportes-adultomayor',
  templateUrl: './reportes-adultomayor.component.html',
  styleUrls: ['./reportes-adultomayor.component.scss']
})
export class ReportesAdultomayorComponent implements OnInit {

  form: UntypedFormGroup;
  public submitted = false;
  
  public hasError = hasError;

  REPORT_TYPES = OLDERADULT_REPORT_TYPES;

  minDate = MIN_DATE;

  constructor(
    private readonly formBuilder: UntypedFormBuilder,
    private readonly olderadultReportsService: OlderadultReportsService,
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

  generateOlderadultReport() {
    this.submitted = true;
    if (this.form.valid) {
      const params = {
        startDate: this.form.controls.startDate.value,
        endDate: this.form.controls.endDate.value
      }
      const olderadultReportId = this.form.controls.reportType.value;
      switch (olderadultReportId) {
        case 1:
          this.olderadultReportsService.getOlderAdultsOutpatientReport(params, `${this.REPORT_TYPES[0].description}.xls`).subscribe();
          break;
        case 2:
          this.olderadultReportsService.getOlderAdultsHospitalizationReport(params, `${this.REPORT_TYPES[1].description}.xls`).subscribe();
          break;
        case 3:
          this.olderadultReportsService.getPolypharmacyReport(params, `${this.REPORT_TYPES[2].description}.xls`).subscribe();
          break;
        default:
      }
    }
  }

}
