import { Component, Input, OnInit } from '@angular/core';
import { Episode } from '../emergency-care-patients-summary/emergency-care-patients-summary.component';
import { EstadosEpisodio } from '../../constants/masterdata';

@Component({
  selector: 'app-emergency-care-status-summary-item',
  templateUrl: './emergency-care-status-summary-item.component.html',
  styleUrls: ['./emergency-care-status-summary-item.component.scss']
})
export class EmergencyCareStatusSummaryItemComponent implements OnInit {

  readonly estadosEpisodio = EstadosEpisodio;
  @Input() episode: Episode;
  
  constructor() { }

  ngOnInit(): void {
  }

}
