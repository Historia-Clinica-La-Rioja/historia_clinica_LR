import { Component, Input } from '@angular/core';
import { ExternalPatientCoverageDto } from '@api-rest/api-model';

@Component({
  selector: 'app-medical-coverage-summary-view',
  templateUrl: './medical-coverage-summary-view.component.html',
  styleUrls: ['./medical-coverage-summary-view.component.scss']
})
export class MedicalCoverageSummaryViewComponent {

  @Input() patientCoverageInfo: ExternalPatientCoverageDto;
  @Input() title = "ambulatoria.medical-coverage-summary-view.TITLE";

  constructor() { }

}
