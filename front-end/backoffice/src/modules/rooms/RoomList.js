import React from 'react';
import {Datagrid, Filter, List, ReferenceField, TextField, TextInput} from 'react-admin';

import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';
import SgxDateField from "../../dateComponents/sgxDateField";

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
            <SgxDateField source="dischargeDate" />
            <ReferenceField source="sectorId" reference="sectors">
                <TextField source="description"/>
            </ReferenceField>
        </Datagrid>
    </List>
);

export default InstitutionList;
