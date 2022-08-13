import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-titled-content-card',
  templateUrl: './titled-content-card.component.html',
  styleUrls: ['./titled-content-card.component.scss']
})
export class TitledContentCardComponent {

  @Input() title = '';

}
