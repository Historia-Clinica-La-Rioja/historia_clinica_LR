import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ERole } from '@api-rest/api-model';
import { AccountService } from '@api-rest/services/account.service';
import { PermissionsService } from '@core/services/permissions.service';
import { NoWhitespaceValidator } from '@core/utils/form.utils';
import { REGISTER_EDITOR_CASES, RegisterDerivationEditor } from '@presentation/components/register-editor-info/register-editor-info.component';

const DERIVACION_DE_DOMINIO = 'DE DOMINIO';
const DOMAIN = 'DOMAIN';
const GESTORES = [ERole.GESTOR_DE_ACCESO_DE_DOMINIO, ERole.GESTOR_DE_ACCESO_LOCAL, ERole.GESTOR_DE_ACCESO_REGIONAL];

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
  showDeriveRequest = false;
  showDeriveForm = false;
  editingDerivation = false;
  REGISTER_EDITOR_CASES_DATE_HOUR = REGISTER_EDITOR_CASES.DATE_HOUR;
  @Input() set derivation(derivation: string) {
		if (derivation) {
			this._derivation = derivation;
      this.showDeriveRequest = true;
      this.setActions();
		}
	};
  @Input() registerDerivationEditor?: RegisterDerivationEditor;
	@Output() derivationEmmiter: EventEmitter<[string, boolean]> = new EventEmitter<[string, boolean]>();

  constructor(private readonly formBuilder: FormBuilder,
    private accountService: AccountService,
    private readonly permissionService: PermissionsService,
  ) { }

  ngOnInit(): void {
    this.formDeriveRequest = this.formBuilder.group({
			coment: new FormControl(this._derivation, { validators: [Validators.required, NoWhitespaceValidator()] })
		});
    if (this.showDeriveRequest) {
      this.getDerivationType();
      this.editDerivationAvailable();
    }
    this.setActions();
  }

  ngOnChanges(): void {
    if (this.registerDerivationEditor) {
      this.getDerivationType();
      this.showDeriveRequest = true;
      this.setActions();
    }
  }

  setActions(): void {
    //DOMAIN
    this.permissionService.hasContextAssignments$([GESTORES[0]]).subscribe(hasRole => { if(hasRole) this.canDerive = false });
    if (this.registerDerivationEditor) {
      //LOCAL
      this.permissionService.hasContextAssignments$([GESTORES[1]]).subscribe(hasRole => { if(hasRole) this.canDerive = false });
      //REGIONAL
      this.permissionService.hasContextAssignments$([GESTORES[2]]).subscribe(hasRole => {
        if (hasRole && this.registerDerivationEditor?.type === DOMAIN) { this.canDerive = false; }
      });
    }
  }

  editDerivationAvailable(): void {
    this.accountService.getInfo().subscribe(userInfo => {
      if (this.registerDerivationEditor.userId === userInfo.id) this.canEditDerivation = true;
    });
  }

  getDerivationType(): void {
    if (this.registerDerivationEditor?.type === DOMAIN) this.registerDerivationEditor.derivationType = DERIVACION_DE_DOMINIO;
    else this.registerDerivationEditor.derivationType = this.registerDerivationEditor?.type;
  }

  openFormToDeriveRequest(): void {
    this.showDeriveRequest = false;
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
    this.canEditDerivation = true;
    this.setActions();
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
