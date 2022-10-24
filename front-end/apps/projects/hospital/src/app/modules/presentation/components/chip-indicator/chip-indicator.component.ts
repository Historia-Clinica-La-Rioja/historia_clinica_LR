import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-chip-indicator',
  templateUrl: './chip-indicator.component.html',
  styleUrls: ['./chip-indicator.component.scss']
})
export class ChipIndicatorComponent {

  @Input() warnColor = false;
  @Input() text: string;

}
