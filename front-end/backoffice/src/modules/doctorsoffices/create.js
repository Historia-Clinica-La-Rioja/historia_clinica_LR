import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required,
} from 'react-admin';
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";
import CustomToolbar from "../../modules/components/CustomToolbar";

const redirect = (basePath, id, data) => `/clinicalspecialtysectors/${data.clinicalSpecialtySectorId}/show`;

const DoctorsOfficeCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
            <TextInput source="description" validate={[required()]} />
            <SgxSelectInput source="clinicalSpecialtySectorId"
                            element="clinicalspecialtysectors"
                            optionText="description"
                            alwaysOn
                            allowEmpty={false}
                            options={{ disabled: true }}/>

            <SgxSelectInput source="institutionId"
                            element="institutions"
                            optionText="name"
                            alwaysOn
                            allowEmpty={false}
                            validate={[required()]}
                            options={{ disabled: true }}/>
            <TextInput source="topic" validate={[required()]} />
        </SimpleForm>
    </Create>
);

export default DoctorsOfficeCreate;