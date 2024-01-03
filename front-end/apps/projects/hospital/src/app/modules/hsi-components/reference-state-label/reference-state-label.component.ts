import { Component, Input, OnInit } from '@angular/core';
import { EReferenceClosureType } from '@api-rest/api-model';
import { Color } from '@presentation/colored-label/colored-label.component';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
  selector: 'app-reference-state-label',
  templateUrl: './reference-state-label.component.html',
  styleUrls: ['./reference-state-label.component.scss'],
  standalone: true,
	imports: [PresentationModule]
})
export class ReferenceStateLabelComponent implements OnInit {

  coloredIconText: ColoredIconText;
  readonly Color = Color;

  @Input() set reference (referenceClosure: EReferenceClosureType) {
    this.coloredIconText = {
      icon: 'swap_horiz',
      color: referenceClosure ? this.Color.GREEN : this.Color.YELLOW,
      text: referenceClosure ? referenceClosure.description : 'turnos.search_references.REFERENCE_REQUESTED'
    }
  }

  constructor() { }

  ngOnInit(): void {
  }

}
