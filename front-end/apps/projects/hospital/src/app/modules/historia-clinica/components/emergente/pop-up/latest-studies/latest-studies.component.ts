import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog'; 

@Component({
  selector: 'app-latest-studies',
  templateUrl: './latest-studies.component.html',
  styleUrls: ['./latest-studies.component.scss']
})
export class LatestStudiesComponent {
  selectedOption: string = '';
  patientId: number;
  
  constructor(
    private dialogRef: MatDialogRef<LatestStudiesComponent>
    
  ) { }

  ngOnInit(): void {
  }

  closeModal(simpleClose: boolean, completed?: boolean): void {
    if (simpleClose) {
      this.dialogRef.close();  
    } else {
     }
  }
}

