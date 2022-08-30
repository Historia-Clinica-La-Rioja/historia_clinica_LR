import { Component, Input } from '@angular/core';
import { Content } from '@presentation/components/indication/indication.component';

@Component({
  selector: 'app-nursing-record',
  templateUrl: './nursing-record.component.html',
  styleUrls: ['./nursing-record.component.scss']
})
export class NursingRecordComponent {

  @Input() nursingSections: NursingSections[];

  constructor() { }

}

export interface NursingSections {
  title: string;
  records: NursingRecord[];
  time?: number;
}

export interface NursingRecord {
  matIcon: string;
  svgIcon?: string;
  content: Content;
}