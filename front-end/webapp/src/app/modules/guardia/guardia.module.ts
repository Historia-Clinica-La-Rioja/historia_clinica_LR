import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GuardiaRoutingModule } from './guardia-routing.module';
import { HomeComponent } from './routes/home/home.component';


@NgModule({
	declarations: [HomeComponent],
	imports: [
		CommonModule,
		GuardiaRoutingModule
	]
})
export class GuardiaModule {
}
