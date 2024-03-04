import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput
} from 'react-admin';

const DepartmentFilter = (props) => (
    <Filter {...props}>
        <TextInput source="description" />
    </Filter>
);

const DepartmentList = props => (
    <List {...props} filters={<DepartmentFilter />} bulkActionButtons={false}>
        <Datagrid rowClick={null}>
            <TextField source="description" />
        </Datagrid>
    </List>
);

export default DepartmentList;
