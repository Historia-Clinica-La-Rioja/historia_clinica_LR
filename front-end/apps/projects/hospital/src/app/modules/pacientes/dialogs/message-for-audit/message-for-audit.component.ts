import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-message-for-audit',
  templateUrl: './message-for-audit.component.html',
  styleUrls: ['./message-for-audit.component.scss']
})
export class MessageForAuditComponent implements OnInit {

  form: FormGroup;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { initialMessage: string },
    public dialog: MatDialogRef<MessageForAuditComponent>,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      message: [this.data.initialMessage, Validators.required],
    });
  }

  close(): void {
    if (this.data.initialMessage === this.form.value.message) {
      this.dialog.close(null);
    }
    else {
      this.dialog.close(this.form.value.message);
    }
  }

}
