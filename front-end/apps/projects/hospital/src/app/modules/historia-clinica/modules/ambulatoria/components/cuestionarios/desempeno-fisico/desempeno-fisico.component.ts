import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-desempeno-fisico',
  templateUrl: './desempeno-fisico.component.html',
  styleUrls: ['./desempeno-fisico.component.scss']
})
export class DesempenoFisicoComponent implements OnInit {

  selectedoptionA: string = '';

  constructor() { }

  ngOnInit(): void {
    
  }


isSubmitDisabled(): boolean {
  return! (
this.selectedoptionA === '1A' || this.selectedoptionA === '2A'

  )
}














  
}
