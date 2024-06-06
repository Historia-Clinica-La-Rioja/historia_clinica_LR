import { Component, Input } from '@angular/core';
import { Position } from '../identifier/identifier.component';

@Component({
  selector: 'app-details-section-custom',
  templateUrl: './details-section-custom.component.html',
  styleUrls: ['./details-section-custom.component.scss']
})
export class DetailsSectionCustomComponent {
  @Input() details: Detail[];
  @Input() layout: Position;
  constructor() { }

}
export interface Detail {
  title: string,
  text: string
}
