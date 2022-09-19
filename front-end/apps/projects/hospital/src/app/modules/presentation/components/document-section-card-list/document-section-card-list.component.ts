import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-document-section-card-list',
  templateUrl: './document-section-card-list.component.html',
  styleUrls: ['./document-section-card-list.component.scss']
})
export class DocumentSectionCardListComponent {

  @Input() title = '';

}
