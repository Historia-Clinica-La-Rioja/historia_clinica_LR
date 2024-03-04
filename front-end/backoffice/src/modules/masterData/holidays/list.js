import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput,
} from 'react-admin';
import {
    SgxDateField,
} from '../../components';

const HolidayFilter = (props) => (
    <Filter {...props}>
        <TextInput source="description" />
    </Filter>
);

const HolidayList = props => (
    <List {...props} filters={<HolidayFilter />}>
        <Datagrid rowClick="show">
            <TextField source="description" />
            <SgxDateField source="date" />
        </Datagrid>
    </List>
);

export default HolidayList;