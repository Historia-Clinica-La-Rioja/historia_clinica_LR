import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { hasError } from '@core/utils/form.utils';
import { DENGUE_REPORT_TYPES } from './constants/dengue-report-types';
import { DengueReportsService } from '@api-rest/services/dengue-reports.service';

@Component({
  selector: 'app-reporte-dengue',
  templateUrl: './reporte-dengue.component.html',
  styleUrls: ['./reporte-dengue.component.scss']
})
export class ReporteDengueComponent implements OnInit {

  form: UntypedFormGroup;
  public submitted = false;
  
  public hasError = hasError;
  
  REPORT_TYPES = DENGUE_REPORT_TYPES;
    
  constructor(
    private readonly formBuilder: UntypedFormBuilder,
    private readonly dengueReportsService: DengueReportsService,
  ) { }
  
  ngOnInit(): void {
    this.form = this.formBuilder.group({
      reportType: [null, Validators.required],
    });
  }

  generateDengueReport() {
    this.submitted = true;
    if(this.form.valid) {
      const params = {

      }
      const dengueReportId = this.form.controls.reportType.value;
      switch (dengueReportId) {
        case 1:
          this.dengueReportsService.getDengueAttentionsReport(params, `${this.REPORT_TYPES[0].description}.xls`).subscribe();
          break;
        case 2:
          this.dengueReportsService.getDengueControlsReport(params, `${this.REPORT_TYPES[1].description}.xls`).subscribe();
          break;
        default:
      }
    }
  }

}
