import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required,
} from 'react-admin';
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";
import SaveCancelToolbar from "../../modules/components/save-cancel-toolbar";

const DoctorsOfficeCreate = props => (
    <Create {...props}>
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
                            allowEmpty={false}
                            validate={[required()]}/>

        </SimpleForm>
    </Create>
);

export default DoctorsOfficeCreate;