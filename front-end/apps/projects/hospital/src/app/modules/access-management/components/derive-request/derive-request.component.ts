import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { EReferenceForwardingType, ERole } from '@api-rest/api-model';
import { NoWhitespaceValidator } from '@core/utils/form.utils';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { AccountService } from '@api-rest/services/account.service';
import { PermissionsService } from '@core/services/permissions.service';

const DERIVACION_DE_DOMINIO = 'DE DOMINIO';
const DOMAIN = 'DOMAIN';

@Component({
  selector: 'app-derive-request',
  templateUrl: './derive-request.component.html',
  styleUrls: ['./derive-request.component.scss']
})
export class DeriveRequestComponent implements OnInit {

  canEditDerivation = false;
  canDerive = true;

  formDeriveRequest: FormGroup<FormDeriveRequest>;
  _derivation = '';
  derivationEditor: RegisterDerivationEditor;
  showDeriveRequest = false;
  showDeriveForm = false;
  editingDerivation = false;
  REGISTER_EDITOR_CASES_DATE_HOUR = REGISTER_EDITOR_CASES.DATE_HOUR;
  @Input() set derivation(derivation: string) {
		if (derivation) {
			this._derivation = derivation;
      this.showDeriveRequest = true;
      this.hideButtonByGestor();
		}
	};
  @Input() set registerDerivationEditor(derivationEditor: RegisterDerivationEditor) {
    if (derivationEditor) {
      this.derivationEditor = derivationEditor;
      this.getDerivationType();
      this.showDeriveRequest = true;
      this.hideButtonByGestor();
    }
  }
	@Output() derivationEmmiter: EventEmitter<DerivationEmmiter> = new EventEmitter<DerivationEmmiter>();

  constructor(private readonly formBuilder: FormBuilder,
    private readonly accountService: AccountService,
    private readonly permissionService: PermissionsService,
  ) { }

  ngOnInit(): void {
    this.formDeriveRequest = this.formBuilder.group({
			comment: new FormControl(this._derivation, { validators: [Validators.required, NoWhitespaceValidator()] })
		});
    if (this.showDeriveRequest) {
      this.checkIfCanEditDerivation();
    }
    this.hideButtonByGestor();
  }

  hideButtonByGestor(): void {
    this.permissionService.contextAssignments$().subscribe(role => { 
      if (role.includes(ERole.GESTOR_DE_ACCESO_DE_DOMINIO)) this.canDerive = false;

      if (this.derivationEditor){
        if (role.includes(ERole.GESTOR_DE_ACCESO_LOCAL)) this.canDerive = false;
        if (role.includes(ERole.GESTOR_DE_ACCESO_REGIONAL) && this.derivationEditor?.type === DOMAIN) this.canDerive = false;
      }
    });
  }

  checkIfCanEditDerivation(): void {
    this.accountService.getInfo().subscribe(userInfo => {
      if (this.derivationEditor.userId === userInfo.id) this.canEditDerivation = true;
    });
  }

  getDerivationType(): void {
    if (this.derivationEditor?.type === DOMAIN) this.derivationEditor.derivationType = DERIVACION_DE_DOMINIO;
    else this.derivationEditor.derivationType = this.derivationEditor?.type;
  }

  openFormToDeriveRequest(): void {
    this.showDeriveRequest = false;
    this.showDeriveForm = true;
  }

  closeDerivationForm(): void {
    this.showDeriveForm = false;
    if(this.editingDerivation) this.editingDerivation = false;
    if (this.derivationEditor) this.showDeriveRequest = true;
  }

  performDerivation(): void {
    this._derivation = this.formDeriveRequest.value.comment;
		this.showDeriveForm = false;
    this.derivationEmmiter.emit({derivation: this._derivation, canEdit: this.editingDerivation});
    this.editingDerivation = false;
    this.canEditDerivation = true;
    this.hideButtonByGestor();
  }

  enableEditDerivation(): void {
    this.showDeriveRequest = false;
    this.showDeriveForm = true;
    this.editingDerivation = true;
  }

}

interface FormDeriveRequest {
  comment: FormControl<string | null>;
}

export interface DerivationEmmiter {
  derivation: string;
  canEdit: boolean;
}

export interface RegisterDerivationEditor extends RegisterEditor {
	id: number;
	type: EReferenceForwardingType;
	derivationType?: string;
	userId: number;
}
