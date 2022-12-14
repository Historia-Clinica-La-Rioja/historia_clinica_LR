import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { hasError } from '@core/utils/form.utils';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

@Component({
  selector: 'app-attach-document-popup',
  templateUrl: './attach-document-popup.component.html',
  styleUrls: ['./attach-document-popup.component.scss']
})
export class AttachDocumentPopupComponent implements OnInit {

  hasError = hasError;
  form: FormGroup;
  documentTypes: TypeaheadOption<any>[];
  required: boolean = true;
  response = [
    {
      type: "Epicrisis",
    },
    {
      type: "Internación",
    },
  ]

  constructor(private fb: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      fileName: new FormControl({ value: this.data.fileName, disabled: true }),
      type: new FormControl(null, Validators.required)
    });
    this.setDocumentTypesFilter();
  }

  setDocumentTypesFilter() {
    const options: TypeaheadOption<any>[] = this.setFilterValues(this.response);
    this.documentTypes = options;
  }

  setFilterValues(response) {
    const opt: TypeaheadOption<any>[] = [];
    response.map(value => {
      opt.push({
        value: value.type,
        compareValue: value.type,
        viewValue: value.type,
      });
    })
    return opt;
  }

  save() {}

  setDocumentType(type: string) {
    this.form.get('type').setValue(type);
  }

}