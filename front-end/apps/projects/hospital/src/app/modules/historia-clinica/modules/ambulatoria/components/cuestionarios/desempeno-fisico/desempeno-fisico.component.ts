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
  selectedoptionE: string = '';
  selectedoptionB2: string = '';
  selectedoptionE2: string = '';
  counterB2: number = 0;
  counterC2: number = 0;
  counterC3: number = undefined;
  counterE1: number = undefined;
  totalScore: number = 0;
  Calculefinal: any;
  finalScore: number = 0;
  calculatePoints: number;
  ticketNum1: number = 0;
  iconActive: boolean;
  iconImageSource: string = 'assets/icons/icon-07.png';
  customIconSource: string = 'assets/icons/icon-10.png';
  iconImageSource2: string = 'assets/icons/icon-08.png';
  customIconSource2: string = 'assets/icons/icon-09.png';


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

  isCounterDisabledCounterC() : boolean {
    return! (
    this.selectedoptionB2 !== '1C'
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

  isSubmitDisabled5(): boolean {
    return !(
      this.selectedoptionE === '1D'

    )
  }

  isCounterDisabled6(): boolean {
    return !(
      this.selectedoptionE === '1D'

    )
  }

  isCounterDisabled7(): boolean {
    return !(
      this.selectedoptionE2 === '1E'

    )
  }
  calculePoints(): number {
    let balance1 = (this.selectedoptionA === '1A') ? 1 : 0;
    let balance2 = (this.selectedoptionB === '1B') ? 1 : 0;
    let sillaTest1 = (this.selectedoptionE === '1D') ? 1 : 0;
    let sillaTest2 = (this.selectedoptionE2 === '1E') ? 1 : 0;

    
    if (this.selectedoptionA === '2A' || this.selectedoptionA === '3A') {
       this.iconImageSource = this.customIconSource;
    } else {
       this.iconImageSource = 'assets/icons/icon-07.png';
    }

    if (this.selectedoptionB === '2B' || this.selectedoptionB === '3B') {
      this.iconImageSource2 = this.customIconSource2;
   } else {
      this.iconImageSource2 = 'assets/icons/icon-08.png';
   }

    let counterB2Points = 0;
    if (this.counterB2 < 3.0) {
      counterB2Points = 0;
    } else if (this.counterB2 >= 3.0 && this.counterB2 < 10) {
      counterB2Points = 1;
    } else if (this.counterB2 >= 10 && this.counterB2 <= 15) {
      counterB2Points = 2;
    }


    let counterC2Points = 0;
    if (this.counterC2 < 3.0) {
      counterC2Points = 0;

    } else if (this.counterC2 >= 3.0 && this.counterC2 < 10) {
      counterC2Points = 1;
    } else if (this.counterC2 >= 10 && this.counterC2 <= 15) {
      counterC2Points = 2;
    }

    let counterC3Points = 0;

    if (this.counterC3 < 4.82) {
      counterC3Points = 4;

    } else if (this.counterC3 >= 4.82 && this.counterC3 < 6.2) {
      counterC3Points = 3;

    } else if (this.counterC3 >= 6.2 && this.counterC3 < 8.7) {
      counterC3Points = 2;

    } else if (this.counterC3 >= 8.7) {
      counterC3Points = 1;
    }


    let counterE1Points = 0;
    if (this.counterE1 <= 11.19) {
      counterE1Points = 4
    } else if (this.counterE1 >= 11.2 && this.counterE1 <= 13.69) {
      counterE1Points = 3;
    } else if (this.counterE1 > 13.69 && this.counterE1 <= 16.69) {
      counterE1Points = 2;
    } else if (this.counterE1 > 16.69 && this.counterE1 <= 60) {
      counterE1Points = 1;
    } else if (this.counterE1 > 60.00) {
      counterE1Points = 0;
    }

    let totalScore = balance1 + balance2 +
      sillaTest1 + sillaTest2 + counterB2Points + counterC2Points
      + counterC3Points + counterE1Points;
    
    console.log("balance1: ", balance1)
    console.log("balance2: ", balance2)
    console.log("silla1: ", sillaTest1)
    console.log("silla2: ", sillaTest2)
    console.log("counterB2Points: ", counterB2Points)
    console.log("counterC2Points: ", counterC2Points)
    console.log("counterC3Points: ", counterC3Points)
    console.log("counterE1Points: ", counterE1Points)
    console.log("Trae: ", totalScore)

      this.finalScore = totalScore;

      this.Calculefinal = (this.finalScore >= 8) ? '1F' : '2F';

    return totalScore;

  }

}












