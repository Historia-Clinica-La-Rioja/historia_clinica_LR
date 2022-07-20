import React from 'react';
import {
    List,
    Datagrid,
    ReferenceField,
    TextField,
    Filter,
    TextInput
} from 'react-admin';

import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const SectorFilter = props =>(
    <Filter {...props}>
        <TextInput source="description" />
        <SgxSelectInput source="institutionId" element="institutions" optionText="name" allowEmpty={false} />
    </Filter>
);


const SectorList = props => (
    <List {...props} hasCreate={false} filters={<SectorFilter />} >
        <Datagrid rowClick="show">
            <TextField source="description" />
            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>
            <ReferenceField source="sectorTypeId"  link={false}  reference="sectortypes">
                <TextField source="description" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export default SectorList;

