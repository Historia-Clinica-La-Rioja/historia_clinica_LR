import React from 'react';
import {
    List,
    Datagrid,
    ReferenceField,
    TextField,
    Filter
} from 'react-admin';

import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const SectorFilter = props =>(
    <Filter {...props}>
        <SgxSelectInput source="institutionId" element="institutions" optionText="name" alwaysOn allowEmpty={false}/>
    </Filter>
);


const SectorList = props => (
    <List {...props} hasCreate={false} filters={<SectorFilter />} >
        <Datagrid rowClick="show">
            <TextField source="description" />
            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export default SectorList;

