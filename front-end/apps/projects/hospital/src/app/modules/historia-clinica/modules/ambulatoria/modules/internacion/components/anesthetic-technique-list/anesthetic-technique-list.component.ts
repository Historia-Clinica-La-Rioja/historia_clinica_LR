import { Component, Input, OnInit } from '@angular/core';
import { AnestheticTechniqueData, AnestheticTechniqueService } from '../../services/anesthetic-technique.service';

@Component({
  selector: 'app-anesthetic-technique-list',
  templateUrl: './anesthetic-technique-list.component.html',
  styleUrls: ['./anesthetic-technique-list.component.scss']
})
export class AnestheticTechniqueListComponent implements OnInit {

  @Input() service: AnestheticTechniqueService ;
  anestheticTechniqueList: AnestheticTechniqueData[]

  constructor(
  ) { }

  ngOnInit(): void {
    this.service.getAnestheticTechniqueList().subscribe(
      anestheticTechniqueList => this.anestheticTechniqueList= anestheticTechniqueList
    )
  }

}
