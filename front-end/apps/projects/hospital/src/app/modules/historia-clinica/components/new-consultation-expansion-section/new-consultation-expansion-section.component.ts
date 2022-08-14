import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-new-consultation-expansion-section',
  templateUrl: './new-consultation-expansion-section.component.html',
  styleUrls: ['./new-consultation-expansion-section.component.scss']
})
export class NewConsultationExpansionSectionComponent {

  private _fixedExpanded = false;
  @Input() icon: string;
  @Input() title: string;
  @Input() hideBorder = false;
  @Input() recommend = false;
  @Input() collapsed = true;
  @Output() collapsedChange = new EventEmitter<boolean>();
  @Input() isEmpty = true;
  @Input() set fixedExpanded(value: boolean) {
    if (this._fixedExpanded && !value) {
      this.collapsed = false;
      this.collapsedChange.emit(this.collapsed);
    }
    this._fixedExpanded = value;
  }

  get fixedExpanded() {
    return this._fixedExpanded;
  }

  toggle(): void {
    if (!this.fixedExpanded) {
      this.collapsed = !this.collapsed
      this.collapsedChange.emit(this.collapsed);
    }
  }

}
