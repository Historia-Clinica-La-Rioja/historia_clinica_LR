import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewViolentPersonInfomationComponent } from '../../dialogs/new-violent-person-infomation/new-violent-person-infomation.component';
import { CustomViolenceReportAggressorDto, ViolenceAggressorsNewConsultationService } from '../../services/violence-aggressors-new-consultation.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-violence-situation-violent-person-information',
  templateUrl: './violence-situation-violent-person-information.component.html',
  styleUrls: ['./violence-situation-violent-person-information.component.scss']
})
export class ViolenceSituationViolentPersonInformationComponent  {
  aggressorsList: CustomViolenceReportAggressorDto[];
  @Input() confirmForm: Observable<boolean>;
	@Output() aggressorsListInfo = new EventEmitter<any>();
  constructor(private readonly dialog: MatDialog, private readonly violenceAggressorsNewConsultationService: ViolenceAggressorsNewConsultationService) {
    this.setAggressors();
   }

   ngOnChanges(changes: SimpleChanges) {
		if(!changes.confirmForm.isFirstChange()){
			this.aggressorsListInfo.emit(this.aggressorsList);
		}
	}
  
  setAggressors() {
    this.violenceAggressorsNewConsultationService.violenceAggressors$.subscribe((concepts: CustomViolenceReportAggressorDto[]) => this.aggressorsList = concepts);
  }

  openNewViolentPerson() {
    this.dialog.open(NewViolentPersonInfomationComponent, {
      autoFocus: false,
      disableClose: true,
      width: '50%',
    })
  }

}
