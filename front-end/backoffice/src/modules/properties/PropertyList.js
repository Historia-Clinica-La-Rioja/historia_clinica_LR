import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    TextInput, Filter, DateField,
} from 'react-admin';

const PropertyFilter = (props) => (
    <Filter {...props}>
        <TextInput source="property" alwaysOn />
        <TextInput source="nodeId" />
    </Filter>
);

const PropertyList = props => (
    <List {...props} filters={<PropertyFilter />} sort={{ field: 'property', order: 'DESC' }} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="property" sortable={false}/>
            <TextField source="value" sortable={false}/>
            <TextField source="nodeId" sortable={false}/>
            <TextField source="description" sortable={false}/>
            <TextField source="origin" sortable={false}/>
            <DateField source="updatedOn" showTime />
        </Datagrid>
    </List>
);

export default PropertyList;
