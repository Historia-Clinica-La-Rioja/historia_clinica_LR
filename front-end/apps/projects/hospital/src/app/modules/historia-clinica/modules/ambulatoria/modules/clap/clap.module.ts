import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClapComponent } from './components/clap/clap.component';
import { PresentationModule } from '@presentation/presentation.module';
import { CoreModule } from '@core/core.module';



@NgModule({
  declarations: [
    ClapComponent
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
