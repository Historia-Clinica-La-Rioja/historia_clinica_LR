import { Component, Input } from '@angular/core';
import { Content } from '@presentation/components/indication/indication.component';
import { NursingSections } from '../specific-nursing-record/specific-nursing-record.component';

@Component({
  selector: 'app-nursing-record',
  templateUrl: './nursing-record.component.html',
  styleUrls: ['./nursing-record.component.scss']
})
export class NursingRecordComponent {

  @Input() nursingRecords: NursingSections[];

  constructor() { }

}

export interface NursingRecord {
  matIcon: string;
  svgIcon?: string;
  content: Content;
}