import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AppointmentsService } from '@api-rest/services/appointments.service';

@Component({
  selector: 'app-confirm-print-appointment',
  templateUrl: './confirm-print-appointment.component.html',
  styleUrls: ['./confirm-print-appointment.component.scss']
})
export class ConfirmPrintAppointmentComponent {

	constructor(
		public dialogRef: MatDialogRef<ConfirmPrintAppointmentComponent>,
		private readonly appointmentService: AppointmentsService,
		@Inject(MAT_DIALOG_DATA) public data: {
			title: string,
			content: string,
			appointmentId: number,
		}
	) {
	}

  getAppointmentTicketReport(): void {
	this.appointmentService.getAppointmentTicketPdf(this.data.appointmentId).subscribe((pdf) => {
		const file = new Blob([pdf], { type: 'application/pdf' });
		const blobUrl = URL.createObjectURL(file);
		const div = document.querySelector("#pdfPrinter");
		const iframe = document.createElement("iframe");
		iframe.setAttribute("src", blobUrl);
		div.appendChild(iframe);
		iframe.contentWindow.print();
	});
}

}
