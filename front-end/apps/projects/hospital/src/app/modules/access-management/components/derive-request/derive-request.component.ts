import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { NoWhitespaceValidator } from '@core/utils/form.utils';

@Component({
  selector: 'app-derive-request',
  templateUrl: './derive-request.component.html',
  styleUrls: ['./derive-request.component.scss']
})
export class DeriveRequestComponent implements OnInit {

  formDeriveRequest: FormGroup<FormDeriveRequest>;
  _derivation = '';
  showDeriveRequest = false;
  showDeriveForm = false;
  @Input() set derivation(derivation: string) {
		if (derivation) {
			this._derivation = derivation;
      this.showDeriveRequest = true;
		}
	};
	@Output() derivationEmmiter: EventEmitter<string> = new EventEmitter<string>();

  constructor(private readonly formBuilder: FormBuilder,) { }

  ngOnInit(): void {
    this.formDeriveRequest = this.formBuilder.group({
			coment: new FormControl(this._derivation, { validators: [Validators.required, NoWhitespaceValidator()] })
		});
  }

  openFormToDeriveRequest() {
    this.showDeriveForm = true;
  }

  cancelDerivation() {
    this.showDeriveForm = false;
  }

  saveDerivation() {
    this._derivation = this.formDeriveRequest.value.coment;
    this.showDeriveRequest = true;
		this.showDeriveForm = false;
    this.derivationEmmiter.emit(this._derivation);
  }

}

interface FormDeriveRequest {
  coment: FormControl<string | null>;
}
