import { Component, Input } from '@angular/core';
import { Color } from '@presentation/colored-label/colored-label.component';

@Component({
  selector: 'app-colored-icon-text',
  templateUrl: './colored-icon-text.component.html',
  styleUrls: ['./colored-icon-text.component.scss']
})
export class ColoredIconTextComponent {

  @Input() coloredIconText: ColoredIconText;

}

export interface ColoredIconText {
  icon: string;
  text: string;
  color: Color;
}
