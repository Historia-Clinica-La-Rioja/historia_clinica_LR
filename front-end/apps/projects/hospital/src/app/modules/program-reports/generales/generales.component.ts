import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { hasError } from '@core/utils/form.utils';
import { GeneralReportService } from '@api-rest/services/general.reports.service';
import { GENERAL_REPORT_TYPES } from '../constants/report-types';


@Component({
  selector: 'app-generales',
  templateUrl: './generales.component.html',
  styleUrls: ['./generales.component.scss']
})
export class GeneralesComponent implements OnInit {

  form: FormGroup;
  public submitted = false;

  public hasError = hasError;

  GENERAL_REPORT_TYPES = GENERAL_REPORT_TYPES;


  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly generalService: GeneralReportService,
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      programReportType: [null, Validators.required],
    });
  }
  generateProgramReport() {
    this.submitted = true;
    if (this.form.valid) {
      const params = {

      }
      const programReportId = this.form.controls.programReportType.value;
      switch (programReportId) {
        case 1:
          this.generalService.getDaiylyEmergencyReport(params, `${this.GENERAL_REPORT_TYPES[0].description}.xls`).subscribe();
          break;
        case 2:
          this.generalService.getDiabeticsReport(params, `${this.GENERAL_REPORT_TYPES[1].description}.xls`).subscribe();
        break;
        case 3:
          this.generalService.getHypertensiveReport(params, `${this.GENERAL_REPORT_TYPES[2].description}.xls`).subscribe();
        break;
        
        default:
      }
    }
  }
}

