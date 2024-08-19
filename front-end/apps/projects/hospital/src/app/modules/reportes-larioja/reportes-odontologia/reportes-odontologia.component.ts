import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { hasError } from '@core/utils/form.utils';
import { ODONTOLOGY_REPORT_TYPES } from './constants/odontology-report-types';
import { MIN_DATE } from '@core/utils/date.utils';
import { OdontologyReportsService } from '@api-rest/services/odontology-reports.service';
import { isSameOrAfter, newDate } from '@core/utils/moment.utils';
import { isBefore } from 'date-fns';
import { fixDate } from '@core/utils/date/format';

@Component({
  selector: 'app-reportes-odontologia',
  templateUrl: './reportes-odontologia.component.html',
  styleUrls: ['./reportes-odontologia.component.scss']
})
export class ReportesOdontologiaComponent implements OnInit {

  form: UntypedFormGroup;
  public submitted = false;

  public hasError = hasError;

  REPORT_TYPES = ODONTOLOGY_REPORT_TYPES;

  minDate = MIN_DATE;

  constructor(
    private readonly formBuilder: UntypedFormBuilder,
    private readonly odontologyReportsService: OdontologyReportsService,
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      reportType: [null, Validators.required],
      startDate: [this.firstDayOfThisMonth(), Validators.required],
      endDate: [this.lastDayOfThisMonth(), Validators.required],
    });
  }

  private firstDayOfThisMonth(): Date {
    const today = newDate();
    return new Date(today.getUTCFullYear(), today.getUTCMonth(), 1);
  }

  private lastDayOfThisMonth(): Date {
    const today = newDate();
    return new Date(today.getUTCFullYear(), today.getUTCMonth() + 1, 0);
  }

  maxStartDate(endDate) {
    const today = newDate();
    return !endDate ? today : isBefore(today, endDate) ? today : endDate;
  }

  checkValidDates() {
    const fixStartDate = fixDate(this.form.value.startDate);
    const fixEndDate = fixDate(this.form.value.endDate);

    this.form.controls.startDate.setValue(fixStartDate);
    this.form.controls.endDate.setValue(fixEndDate);
    // if both are present, check that the end date is not after the start date
    if (this.form.value.startDate && this.form.value.endDate) {
      if (isBefore(fixEndDate, fixStartDate)) {
        this.form.controls.endDate.setErrors({ min: true });
        this.form.controls.startDate.setErrors({ max: true });
      } else {
        this.form.controls.endDate.setErrors(null);
        this.checkStartDateIsSameOrBeforeToday();
      }
    } else if (fixStartDate) {
      this.checkStartDateIsSameOrBeforeToday();
    } else if (fixEndDate) {
      this.form.controls.endDate.setErrors(null);
    }
  }

  private checkStartDateIsSameOrBeforeToday() {
    const today = newDate();
    const startDate = this.form.value.startDate;
    (isSameOrAfter(today, startDate))
      ? this.form.controls.startDate.setErrors(null)
      : this.form.controls.startDate.setErrors({ afterToday: true });
  }

  generateOdontologyReport() {
    this.submitted = true;
    if (this.form.valid) {
      const params = {
        startDate: this.form.controls.startDate.value,
        endDate: this.form.controls.endDate.value
      }
      const odontologyReportId = this.form.controls.reportType.value;
      switch (odontologyReportId) {
        case 1:
          this.odontologyReportsService.getPromocionPrimerNivelReport(params, `${this.REPORT_TYPES[0].description}.xls`).subscribe();
          break;
        case 2:
          this.odontologyReportsService.getPrevencionPrimerNivelReport(params, `${this.REPORT_TYPES[1].description}.xls`).subscribe();
          break;
        case 3:
          this.odontologyReportsService.getPrevencionGrupalPrimerNivelReport(params, `${this.REPORT_TYPES[2].description}.xls`).subscribe();
          break;
        case 4:
          this.odontologyReportsService.getOperatoriaSegundoNivelReport(params, `${this.REPORT_TYPES[3].description}.xls`).subscribe();
          break;
        case 5:
          this.odontologyReportsService.getEndodonciaSegundoNivelReport(params, `${this.REPORT_TYPES[4].description}.xls`).subscribe();
          break;
        case 6:
          this.odontologyReportsService.getOdontologicalProceduresReport(params, `${this.REPORT_TYPES[5].description}.xls`).subscribe();
          break;
        default:
      }
    }
  }

}
