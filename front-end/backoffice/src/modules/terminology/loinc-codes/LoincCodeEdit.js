import React from 'react';
import {
    Edit,
    SimpleForm,
    TextInput,
    AutocompleteInput,
    ReferenceInput
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const LoincCodeEdit = props => {
    return(
    <Edit {...props} >
        <SimpleForm toolbar={<CustomToolbar isEdit={false}/>}>
            <TextInput source="code" disabled={true}/>
            <TextInput source="description" disabled={true}/>
            <ReferenceInput
                source="statusId"
                reference="loinc-statuses">
                <AutocompleteInput optionText="description" optionValue="id" options={{ disabled: true }} />
            </ReferenceInput>
            <ReferenceInput
                source="systemId"
                reference="loinc-systems">
                <AutocompleteInput optionText="description" optionValue="id" options={{ disabled: true }} />
            </ReferenceInput>
            <TextInput source="displayName" disabled={true} format={x => x || ' '}/>
            <TextInput source="customDisplayName"/>
        </SimpleForm>
    </Edit>);
};

export default LoincCodeEdit;