import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";

const EpisodeDocumentTypeEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="description" validate={[required()]} />
        </SimpleForm>
    </Edit>
);

export default EpisodeDocumentTypeEdit;