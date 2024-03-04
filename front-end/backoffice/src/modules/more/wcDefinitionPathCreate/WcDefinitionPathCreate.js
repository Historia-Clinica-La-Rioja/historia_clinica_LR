import React from 'react';

import {
    Create,
    SimpleForm,
    TextInput,
    required,
} from 'react-admin'

import { validateUrl } from '../../../libs/sgx/shared/form-validate';

const WcDefinitionPathCreate = props => {
    return (
    <Create {...props}>
        <SimpleForm redirect="show">
            <TextInput source="name" validate={[required()]}/>
            <TextInput source="path" validate={[required(), validateUrl]}/>
        </SimpleForm>
    </Create>
)};


export default WcDefinitionPathCreate;
