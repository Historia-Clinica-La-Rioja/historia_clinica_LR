import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { NoWhitespaceValidator } from '@core/utils/form.utils';
import { REGISTER_EDITOR_CASES, RegisterDerivationEditor } from '@presentation/components/register-editor-info/register-editor-info.component';

const DERIVACION_DE_DOMINIO = 'DE DOMINIO';

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
  REGISTER_EDITOR_CASES_DATE_HOUR = REGISTER_EDITOR_CASES.DATE_HOUR;
  @Input() set derivation(derivation: string) {
		if (derivation) {
			this._derivation = derivation;
      this.showDeriveRequest = true;
		}
	};
  @Input() registerDerivationEditor?: RegisterDerivationEditor;
	@Output() derivationEmmiter: EventEmitter<string> = new EventEmitter<string>();

  constructor(private readonly formBuilder: FormBuilder,) { }

  ngOnInit(): void {
    this.formDeriveRequest = this.formBuilder.group({
			coment: new FormControl(this._derivation, { validators: [Validators.required, NoWhitespaceValidator()] })
		});
    if (this.showDeriveRequest) this.getDerivationType();
  }

  getDerivationType() {
    if (this.registerDerivationEditor.type === 'DOMAIN') this.registerDerivationEditor.derivationType = DERIVACION_DE_DOMINIO;
    else this.registerDerivationEditor.derivationType = this.registerDerivationEditor.type;
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
