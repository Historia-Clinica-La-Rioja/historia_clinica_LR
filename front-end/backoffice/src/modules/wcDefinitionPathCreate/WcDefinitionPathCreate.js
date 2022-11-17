import React from 'react';

import {
    Create,
    SimpleForm,
    TextInput,
    required
} from 'react-admin'

const WcDefinitionPathCreate = props => {
    return (
    <Create {...props}>
        <SimpleForm>
            <TextInput source="path" validate={[required()]}/>
            <TextInput source="name" validate={[required()]}/>
        </SimpleForm>
    </Create>
)};


export default WcDefinitionPathCreate;
