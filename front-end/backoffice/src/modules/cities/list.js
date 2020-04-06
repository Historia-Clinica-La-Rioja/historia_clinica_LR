import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
} from 'react-admin';

const CityList = props => (
    <List {...props} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="description" />
            <ReferenceField source="departmentId" reference="departments" link={false}>
                <TextField source="description" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export default CityList;