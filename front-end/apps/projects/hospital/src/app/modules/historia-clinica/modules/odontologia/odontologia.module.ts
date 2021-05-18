import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToothComponent } from './components/tooth/tooth.component';
import { FlexLayoutModule, FlexModule } from '@angular/flex-layout';
import { AppMaterialModule } from '@material/app.material.module';
import { OdontogramComponent } from './components/odontogram/odontogram.component';



@NgModule({
	declarations: [ToothComponent, OdontogramComponent],
	imports: [
		CommonModule,
		FlexModule,
		FlexLayoutModule,
		AppMaterialModule
	],
	exports: [
		ToothComponent,
		OdontogramComponent
	]
})
export class OdontologiaModule { }
