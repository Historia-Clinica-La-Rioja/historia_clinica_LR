import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-new-consultation-expansion-section',
  templateUrl: './new-consultation-expansion-section.component.html',
  styleUrls: ['./new-consultation-expansion-section.component.scss']
})
export class NewConsultationExpansionSectionComponent {

  private _fixedExpanded = false;
  @Input() collapsed = true;
  @Input() icon: string;
  @Input() title: string;
  @Input() isEmpty = true;
  @Input() set fixedExpanded(value: boolean) {
    if (this._fixedExpanded && !value) {
      this.collapsed = false;
    }
    this._fixedExpanded = value;
  }

  get fixedExpanded() {
    return this._fixedExpanded;
  }

  toggle(): void {
    if (!this.fixedExpanded) {
      this.collapsed = !this.collapsed
    }
  }

}
