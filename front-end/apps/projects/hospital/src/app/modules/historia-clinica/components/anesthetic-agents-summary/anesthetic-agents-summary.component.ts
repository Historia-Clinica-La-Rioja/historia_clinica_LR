import { Component, Input } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';

@Component({
  selector: 'app-anesthetic-agents-summary',
  templateUrl: './anesthetic-agents-summary.component.html',
  styleUrls: ['./anesthetic-agents-summary.component.scss']
})
export class AnestheticAgentsSummaryComponent {
  @Input() anestheticAgents: DescriptionItemData[];

  constructor() { }
}
