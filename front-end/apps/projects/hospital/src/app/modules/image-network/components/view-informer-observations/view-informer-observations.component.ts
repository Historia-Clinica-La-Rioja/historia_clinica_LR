import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { InformerObservationDto } from '@api-rest/api-model';

@Component({
  selector: 'app-view-informer-observations',
  templateUrl: './view-informer-observations.component.html',
  styleUrls: ['./view-informer-observations.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ViewInformerObservationsComponent implements OnInit {

  @Input() informerObservations: InformerObservationDto
  @Input() disableEditing: boolean = false;
  @Output() updateEditing = new EventEmitter<any>;


  constructor() { }

  ngOnInit(): void {
  }

  enableEditing(): void {
    this.updateEditing.emit()
  }

}
