import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';
import SaveCancelToolbar from "../../modules/components/save-cancel-toolbar";

const ClinicalSpecialtyCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<SaveCancelToolbar />}>
            <TextInput source="name" validate={[required()]} />
            <TextInput source="sctidCode" validate={[required()]} />
        </SimpleForm>
    </Create>
);

export default ClinicalSpecialtyCreate;
