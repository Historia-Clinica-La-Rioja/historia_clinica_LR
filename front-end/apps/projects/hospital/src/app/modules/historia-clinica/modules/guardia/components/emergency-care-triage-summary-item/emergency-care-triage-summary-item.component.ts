import { Component, Input } from '@angular/core';
import { EpisodeListTriage } from '../emergency-care-patients-summary/emergency-care-patients-summary.component';
import { MasterDataDto } from '@api-rest/api-model';

@Component({
  selector: 'app-emergency-care-triage-summary-item',
  templateUrl: './emergency-care-triage-summary-item.component.html',
  styleUrls: ['./emergency-care-triage-summary-item.component.scss']
})
export class EmergencyCareTriageSummaryItemComponent {

  @Input() triage: EpisodeListTriage;
  @Input() episodeType: MasterDataDto;

  constructor() { }

}
