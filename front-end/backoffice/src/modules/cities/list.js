import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    Filter,
    TextInput
} from 'react-admin';

import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const CityFilter = (props) => (
    <Filter {...props}>
        <TextInput source="description" />
        <SgxSelectInput source="departmentId" element="departments" optionText="description" allowEmpty={false} />
    </Filter>
);

const CityList = props => (
    <List {...props} filters={<CityFilter />} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="description" />
            <ReferenceField source="departmentId" reference="departments" link={false}>
                <TextField source="description" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export default CityList;
