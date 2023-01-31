import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClapComponent } from './components/clap/clap.component';
import { PresentationModule } from '@presentation/presentation.module';
import { NewGestationPopupComponent } from './dialogs/new-gestation-popup/new-gestation-popup.component';



@NgModule({
  declarations: [
    ClapComponent,
    NewGestationPopupComponent
  ],
  imports: [
    CommonModule,
	PresentationModule,
  ],
  exports: [
	ClapComponent
  ]
})
export class ClapModule { }
