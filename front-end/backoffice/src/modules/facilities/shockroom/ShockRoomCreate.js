import React from 'react';
import { Create, required, SimpleForm, TextInput } from 'react-admin';

import SgxSelectInput from '../../../sgxSelectInput/SgxSelectInput';
import CustomToolbar from '../../components/CustomToolbar';

const redirect = (basePath, id, data) => `/sectors/${data.sectorId}/show`;

const ShockRoomCreate = (props) => {
    return (
        <Create {...props}>
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
                <TextInput source="description" validate={[required()]} />
                <SgxSelectInput source="sectorId"
                    element="sectors"
                    optionText="description"
                    alwaysOn
                    allowEmpty={false}
                    options={{ disabled: true }} />

                <SgxSelectInput source="institutionId"
                    element="institutions"
                    optionText="name"
                    alwaysOn
                    allowEmpty={false}
                    validate={[required()]}
                    options={{ disabled: true }} />
            </SimpleForm>
        </Create>
    )
}

export default ShockRoomCreate;