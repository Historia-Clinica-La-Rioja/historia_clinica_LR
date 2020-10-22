import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
} from 'react-admin';
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";
import SaveCancelToolbar from "../../modules/components/save-cancel-toolbar";

const DoctorsOfficeEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<SaveCancelToolbar />}>
            <TextInput source="description" validate={[required()]} />
            <SgxSelectInput source="clinicalSpecialtySectorId"
                            element="clinicalspecialtysectors"
                            optionText="description"
                            alwaysOn
                            allowEmpty={false}/>

            <SgxSelectInput source="institutionId"
                            element="institutions"
                            optionText="name"
                            alwaysOn
                            allowEmpty={false}/>
        </SimpleForm>
    </Edit>
);

export default DoctorsOfficeEdit;