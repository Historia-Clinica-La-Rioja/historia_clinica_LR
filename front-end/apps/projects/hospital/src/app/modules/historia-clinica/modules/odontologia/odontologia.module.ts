import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToothComponent } from './components/tooth/tooth.component';
import { FlexLayoutModule, FlexModule } from '@angular/flex-layout';
import { AppMaterialModule } from '@material/app.material.module';



@NgModule({
	declarations: [ToothComponent],
	imports: [
		CommonModule,
		FlexModule,
		FlexLayoutModule,
		AppMaterialModule
	],
	exports: [
		ToothComponent
	]
})
export class OdontologiaModule { }
