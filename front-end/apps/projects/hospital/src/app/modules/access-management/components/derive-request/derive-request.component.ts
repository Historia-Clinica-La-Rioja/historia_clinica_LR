import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AccountService } from '@api-rest/services/account.service';
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
  canEditDerivation = false;
  editingDerivation = false;
  REGISTER_EDITOR_CASES_DATE_HOUR = REGISTER_EDITOR_CASES.DATE_HOUR;
  @Input() set derivation(derivation: string) {
		if (derivation) {
			this._derivation = derivation;
      this.showDeriveRequest = true;
		}
	};
  @Input() registerDerivationEditor?: RegisterDerivationEditor;
	@Output() derivationEmmiter: EventEmitter<[string, boolean]> = new EventEmitter<[string, boolean]>();

  constructor(private readonly formBuilder: FormBuilder, private accountService: AccountService) { }

  ngOnInit(): void {
    this.formDeriveRequest = this.formBuilder.group({
			coment: new FormControl(this._derivation, { validators: [Validators.required, NoWhitespaceValidator()] })
		});
    if (this.showDeriveRequest) {
      this.getDerivationType();
      this.editDerivationAvailable();
    }
  }

  ngOnChanges(): void {
    if (this.registerDerivationEditor) {
      this.getDerivationType();
      this.showDeriveRequest = true;
    }
  }

  editDerivationAvailable(): void {
    this.accountService.getInfo().subscribe(userInfo => {
      if (this.registerDerivationEditor.userId === userInfo.id) this.canEditDerivation = true;
    });
  }

  getDerivationType(): void {
    if (this.registerDerivationEditor?.type === 'DOMAIN') this.registerDerivationEditor.derivationType = DERIVACION_DE_DOMINIO;
    else this.registerDerivationEditor.derivationType = this.registerDerivationEditor?.type;
  }

  openFormToDeriveRequest(): void {
    this.showDeriveForm = true;
  }

  cancelDerivation(): void {
    this.showDeriveForm = false;
  }

  saveDerivation(): void {
    this._derivation = this.formDeriveRequest.value.coment;
		this.showDeriveForm = false;
    this.derivationEmmiter.emit([this._derivation, this.editingDerivation]);
    this.editingDerivation = false;
  }

  setEditDerivation(): void {
    this.showDeriveRequest = false;
    this.showDeriveForm = true;
    this.editingDerivation = true;
  }

}

interface FormDeriveRequest {
  coment: FormControl<string | null>;
}
