import { RouterModule, Routes } from "@angular/router";
import { ReportesLariojaComponent } from "./reportes-larioja.component";
import { NgModule } from "@angular/core";

const routes: Routes = [
    {
        path: '',
        children: [
            {path: '', component: ReportesLariojaComponent},

        ]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ReportesLariojaRoutingModule { }