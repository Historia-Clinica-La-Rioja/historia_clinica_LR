import { Component, Input } from '@angular/core';
import { Detail } from '@presentation/components/details-section-custom/details-section-custom.component';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
  selector: 'app-header-attention-detail',
  templateUrl: './header-attention-detail.component.html',
  styleUrls: ['./header-attention-detail.component.scss'],
  standalone: true,
  imports: [PresentationModule]
})
export class HeaderAttentionDetailComponent {
  @Input() title: string;
  @Input() details: Detail[];

  constructor() { }

}