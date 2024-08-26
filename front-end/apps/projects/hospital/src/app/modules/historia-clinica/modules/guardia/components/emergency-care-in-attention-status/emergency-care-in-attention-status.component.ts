import { Component, Input } from '@angular/core';
import { IDENTIFIER_CASES } from '@hsi-components/identifier-cases/identifier-cases.component';
import { Episode } from '../emergency-care-patients-summary/emergency-care-patients-summary.component';
import { RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { Color } from '@presentation/colored-label/colored-label.component';

@Component({
  selector: 'app-emergency-care-in-attention-status',
  templateUrl: './emergency-care-in-attention-status.component.html',
  styleUrls: ['./emergency-care-in-attention-status.component.scss']
})
export class EmergencyCareInAttentionStatusComponent {

  readonly IDENTIFIER_CASES = IDENTIFIER_CASES;
  readonly GREEN = Color.GREEN;
  attentionCreator: RegisterEditor;
  _episode: Episode;

  @Input() set episode(episode: Episode) {
    this._episode = episode;
    this.attentionCreator = { createdBy: `${episode.relatedProfessional.firstName} ${episode.relatedProfessional.lastName}` };
  };
  constructor() { }

}
