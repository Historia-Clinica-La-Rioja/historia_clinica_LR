import { Component, Input, OnInit } from '@angular/core';
import { AnalgesicTechniqueData, AnalgesicTechniqueService } from '../../services/analgesic-technique.service';

@Component({
  selector: 'app-analgesic-technique-background-list',
  templateUrl: './analgesic-technique-background-list.component.html',
  styleUrls: ['./analgesic-technique-background-list.component.scss']
})
export class AnalgesicTechniqueBackgroundListComponent implements OnInit {

  @Input() service: AnalgesicTechniqueService;
  analgesicTechniqueList: AnalgesicTechniqueData[];

  constructor() { }

  ngOnInit(): void {
    this.service.getAnalgesicTechniqueList().subscribe(aTechniqueList => this.analgesicTechniqueList = aTechniqueList )
  }

}
