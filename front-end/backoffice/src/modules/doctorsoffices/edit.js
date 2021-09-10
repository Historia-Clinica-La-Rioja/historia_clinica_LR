import React from 'react';
import {Edit, required, SimpleForm, TextInput,} from 'react-admin';
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";
import CustomToolbar from "../../modules/components/CustomToolbar";

const DoctorsOfficeEdit = ({ permissions, ...props }) => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
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

            { permissions && permissions.isOn("HABILITAR_LLAMADO") && <TextInput source="topic"/> }

        </SimpleForm>
    </Edit>
);

export default DoctorsOfficeEdit;