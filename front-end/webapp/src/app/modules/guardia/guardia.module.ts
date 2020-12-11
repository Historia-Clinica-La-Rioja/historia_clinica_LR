import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GuardiaRoutingModule } from './guardia-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';


@NgModule({
	declarations: [HomeComponent],
	imports: [
		CommonModule,
		GuardiaRoutingModule,
		CoreModule,
		PresentationModule
	]
})
export class GuardiaModule {
}
