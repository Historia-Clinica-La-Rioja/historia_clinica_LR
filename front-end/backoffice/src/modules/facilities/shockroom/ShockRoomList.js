import React from 'react';
import { Datagrid, Filter, List, ReferenceField, TextField, TextInput } from 'react-admin';

import SgxSelectInput from '../../../sgxSelectInput/SgxSelectInput';

const ShockRoomFilter = props => (
    <Filter {...props}>
        <TextInput source="description" />
        <SgxSelectInput label="Institución" source="institutionId" element="institutions" optionText="name" allowEmpty={false} />
        <SgxSelectInput label="Sector" source="sectorId" element="sectors" optionText="description" allowEmpty={false} />
    </Filter>
);

const ShockRoomList = (props) => {
    return (
        <List {...props} hasCreate={false} filters={<ShockRoomFilter />} filter={{ deleted: false }} >
            <Datagrid rowClick="show">

                <TextField source="description" />

                <ReferenceField source="institutionId" reference="institutions">
                    <TextField source="name" />
                </ReferenceField>

                <ReferenceField source="sectorId" reference="sectors">
                    <TextField source="description" />
                </ReferenceField>
            </Datagrid>
        </List>
    )
};

export default ShockRoomList;