import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatStepperModule } from '@angular/material/stepper';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatStepperModule,
    MatSlideToggleModule,
  ],
  exports: [
    MatStepperModule,
    MatSlideToggleModule,
  ]
})
export class LazyMaterialModule { }
