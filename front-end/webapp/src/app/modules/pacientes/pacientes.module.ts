import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PacientesRoutingModule } from './pacientes-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from "@core/core.module";
import { AppMaterialModule } from "../../app.material.module";
import { PacientesTableComponent } from './component/pacientes-table/pacientes-table.component';
import { SearchCreateComponent } from './component/search-create/search-create.component';
import { SearchComponent } from './routes/search/search.component';
import { PartialMatchesTableComponent } from './component/partial-matches-table/partial-matches-table.component';


@NgModule({
	declarations: [
		HomeComponent,
		PacientesTableComponent,
        SearchCreateComponent,
        SearchComponent,
        PartialMatchesTableComponent,
	],
	imports: [
		AppMaterialModule,
		CoreModule,
		FormsModule,
		PacientesRoutingModule,
        ReactiveFormsModule,
	]
})
export class PacientesModule {
}

export class DatosPersonales {
    firstName:string;
    middleNames:string;
    lastName:string;
    otherLastNames:string;
    identificationTypeId:number;
    identificationNumber:string;
    genderId:number;
    birthDate:Date;
    //Person_extended
    cuil:string;
    mothersLastName:string;
    phoneNumber:string;
    email:string;
    ethnic:string;
    religion:string;
    nameSelfDetermination:string;
    genderSelfDeterminationId:number;
    healthInsuranceId:number;
    //Address
    addressStreet:string;
    addressNumber:string;
    addressFloor:string;
    addressApartment:string;
    addressQuarter:string;
    addressCityId:number;
    addressPostcode:string;
}
