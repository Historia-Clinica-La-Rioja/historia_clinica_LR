import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatStepperModule } from '@angular/material/stepper';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatStepperModule,
  ],
  exports: [
    MatStepperModule,
  ]
})
export class LazyMaterialModule { }
