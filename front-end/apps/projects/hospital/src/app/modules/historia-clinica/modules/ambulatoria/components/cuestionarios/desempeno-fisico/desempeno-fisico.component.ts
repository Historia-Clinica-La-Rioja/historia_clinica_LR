import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-desempeno-fisico',
  templateUrl: './desempeno-fisico.component.html',
  styleUrls: ['./desempeno-fisico.component.scss']
})
export class DesempenoFisicoComponent implements OnInit {

  selectedoptionA: string = '';
  selectedoptionB: string = '';
  selectedoptionC: string = '';
  selectedoptionD: string = '';

  imageInactive1: boolean;
  iconActive: boolean;
  constructor() { }

  ngOnInit(): void {

  }


  isSubmitDisabled(): boolean {
    return !(
      this.selectedoptionA === '1A'


    )
  }

  isCounterDisabled(): boolean {
    return !(

      this.selectedoptionA === '1A' || this.selectedoptionA === '2A'

    )
  }
  isCounterDisabled2(): boolean {
    return !(

      this.selectedoptionB === '1B' || this.selectedoptionB === '2B'

    )

  }

  isSubmitDisabled3(): boolean {
    return !(

      this.selectedoptionB === '1B'
    )

  }

  isCounterDisabled3(): boolean {
    return !(
      this.selectedoptionB === '1B'
    )
  }

  isCounterDisabled4(): boolean {
    return !(
      this.selectedoptionC !== '2C'


    )

  }

  isCounterDisabled5(): boolean {
    return !(
      this.selectedoptionD !== '3C'

    )
  }

  isSubmitDisabled4(): boolean {
    return !(
      this.selectedoptionC !== '2C'

    )
  }





  //   imgActive(value: string) {
  //     this.iconActive = false;
  //     this.imageInactive1 = false;

  //     if (value === '1A') {
  //         this.iconActive = true;
  //     } else if (value === '2A' || value === '3A') {
  //         this.imageInactive1 = true;
  //     }
  // }

}

