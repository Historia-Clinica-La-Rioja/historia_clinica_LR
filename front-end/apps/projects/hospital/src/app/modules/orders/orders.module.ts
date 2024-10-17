import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeComponent } from './routes/home/home/home.component';
import { WorkOrderListComponent } from './routes/home/work-order-list/work-order-list.component';
import { CoreModule } from '@core/core.module';
import { OrderDetailComponent } from './components/order-detail/order-detail.component';
import { PatientSummaryComponent } from '@hsi-components/patient-summary/patient-summary.component';
import { PresentationModule } from '@presentation/presentation.module';
import { OrdersRoutingModule } from './orders-routing.module';
import { PatientLocationComponent } from './components/patient-location/patient-location.component';
import { StudyOrderWorkListDtoToOrderDetailsPipe } from './pipe/study-order-work-list-dto-to-order-details.pipe';
//Standalone Component
import { IdentifierCasesComponent } from '@hsi-components/identifier-cases/identifier-cases.component';

@NgModule({
	declarations: [
		HomeComponent,
		OrderDetailComponent,
 		PatientLocationComponent,
		StudyOrderWorkListDtoToOrderDetailsPipe,
		WorkOrderListComponent,
	],
	imports: [
		CommonModule,
		CoreModule,
		OrdersRoutingModule,
		PresentationModule,
		PatientSummaryComponent,
		//Standalone Component
		IdentifierCasesComponent,
	]
})
export class OrdersModule { }
