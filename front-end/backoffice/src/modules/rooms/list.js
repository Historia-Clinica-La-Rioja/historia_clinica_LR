import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    DateField, 
    Filter,
    TextInput
} from 'react-admin';

import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const RoomFilter = props =>(

    <Filter {...props}>
        <TextInput source="roomNumber" />
        <TextInput source="description" />
        <TextInput source="type" />
        <SgxSelectInput label="Sector" source="sectorId" element="sectors" optionText="description" allowEmpty={false} />
    </Filter>
);

const InstitutionList = props => (
    <List {...props} hasCreate={false} filters={<RoomFilter/>}>
        <Datagrid rowClick="show">
            <TextField source="roomNumber"/>
            <TextField source="description" />
            <TextField source="type" />
            <DateField source="dischargeDate" />
            <ReferenceField source="sectorId" reference="sectors">
                <TextField source="description"/>
            </ReferenceField>
        </Datagrid>
    </List>
);

export default InstitutionList;
