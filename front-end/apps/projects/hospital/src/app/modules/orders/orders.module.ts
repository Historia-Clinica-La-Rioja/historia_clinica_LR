import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeComponent } from './routes/home/home/home.component';
import { WorkOrderListComponent } from './routes/home/work-order-list/work-order-list.component';
import { CoreModule } from '@core/core.module';
import { OrderDetailComponent } from './components/order-detail/order-detail.component';
import { PatientSummaryComponent } from '@hsi-components/patient-summary/patient-summary.component';
import { PresentationModule } from '@presentation/presentation.module';
import { OrdersRoutingModule } from './orders-routing.module';
import { StudyOrderWorkListDtoToOrderDetailsPipe } from './pipe/study-order-work-list-dto-to-order-details.pipe';


@NgModule({
	declarations: [
		HomeComponent,
		WorkOrderListComponent,
		OrderDetailComponent,
		StudyOrderWorkListDtoToOrderDetailsPipe,
	],
	imports: [
		CommonModule,
		CoreModule,
		OrdersRoutingModule,
		PresentationModule,
		PatientSummaryComponent,
	]
})
export class OrdersModule { }
