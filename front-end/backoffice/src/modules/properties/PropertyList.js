import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    TextInput, Filter,
} from 'react-admin';

const PropertyFilter = (props) => (
    <Filter {...props}>
        <TextInput source="id" />
    </Filter>
);

const PropertyList = props => (
    <List {...props} filters={<PropertyFilter />} sort={{ field: 'id', order: 'DESC' }} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="id" sortable={false}/>
            <TextField source="value" sortable={false}/>
            <TextField source="origin" sortable={false}/>
        </Datagrid>
    </List>
);

export default PropertyList;
