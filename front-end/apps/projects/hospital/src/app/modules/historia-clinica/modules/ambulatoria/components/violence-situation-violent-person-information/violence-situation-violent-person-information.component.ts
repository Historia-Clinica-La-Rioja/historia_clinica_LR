import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewViolentPersonInfomationComponent } from '../../dialogs/new-violent-person-infomation/new-violent-person-infomation.component';
import { ViolenceReportAggressorDto } from '@api-rest/api-model';
import { ViolenceAggressorsNewConsultationService } from '../../services/violence-aggressors-new-consultation.service';

@Component({
  selector: 'app-violence-situation-violent-person-information',
  templateUrl: './violence-situation-violent-person-information.component.html',
  styleUrls: ['./violence-situation-violent-person-information.component.scss']
})
export class ViolenceSituationViolentPersonInformationComponent implements OnInit {
  aggressorsList: ViolenceReportAggressorDto[];

  constructor(private readonly dialog: MatDialog, private readonly violenceAggressorsNewConsultationService: ViolenceAggressorsNewConsultationService) {
    this.setAggressors();
   }

  ngOnInit(): void {
  }

  setAggressors() {
    this.violenceAggressorsNewConsultationService.violenceAggressors$.subscribe((concepts: ViolenceReportAggressorDto[]) => this.aggressorsList = concepts);
  }

  openNewViolentPerson() {
    this.dialog.open(NewViolentPersonInfomationComponent, {
      autoFocus: false,
      disableClose: true,
      width: '50%',
    })
  }

}
