import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required
} from 'react-admin';

import CustomToolbar from '../../components/CustomToolbar';
import { validateUrl } from '../../../libs/sgx/shared/form-validate';

const WcDefinitionPathEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/> }>
            <TextInput source="name" validate={[required()]} />
            <TextInput source="path" validate={[required(), validateUrl]} />
        </SimpleForm>
    </Edit>
);

export default WcDefinitionPathEdit;
