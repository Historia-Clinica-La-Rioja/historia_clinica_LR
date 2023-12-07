import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { STUDY_STATUS } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';
import { DiagnosticWithTypeReportInfoDto } from '../../model/ImageModel';

@Component({
  selector: 'app-study-images-reports',
  templateUrl: './study-images-reports.component.html',
  styleUrls: ['./study-images-reports.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StudyImagesReportsComponent implements OnInit {

  STUDY_STATUS = STUDY_STATUS;

  @Input() studyInfo: DiagnosticWithTypeReportInfoDto
  @Input() reportStatus: boolean
  @Input() appointmentId: number
  showAccessToFiles = false
  isImageOrderCasesIncomplete = false

  constructor() { }

  ngOnInit(): void {
    this.isImageOrderCasesIncomplete = this.studyInfo.typeOrder !== 'completa' && !this.studyInfo.infoOrderInstances.status
    this.showAccessToFiles = this.studyInfo.typeOrder === 'completa' && this.studyInfo.statusId === STUDY_STATUS.FINAL_RDI.id
    || this.studyInfo.typeOrder !== 'completa' && this.studyInfo.infoOrderInstances.status
  }

}