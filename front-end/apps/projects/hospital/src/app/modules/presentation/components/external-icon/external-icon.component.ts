import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-external-icon',
  templateUrl: './external-icon.component.html',
  styleUrls: ['./external-icon.component.scss']
})
export class ExternalIconComponent {

  @Input() svgIcon: string;
  constructor() { }

}
