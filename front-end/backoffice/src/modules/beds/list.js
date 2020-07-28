import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    FunctionField,
    BooleanField, Filter
} from 'react-admin';

import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const renderRoom = room => `${room.roomNumber} - ${room.description}`;

const BedFilter = props =>(
    <Filter {...props}>
        <SgxSelectInput source="roomId" element="rooms" optionText="description" allowEmpty={false} />
    </Filter>
);

const BedList = props => (
    <List {...props} hasCreate={false} filters={<BedFilter/>}>
        <Datagrid rowClick="show">
                <TextField source="bedNumber" />
                <ReferenceField source="roomId" reference="rooms">
                    <FunctionField render={renderRoom} />
                </ReferenceField>
                <BooleanField source="enabled" />
                <BooleanField source="available" />
                <BooleanField source="free" />
        </Datagrid>
    </List>
);

export default BedList;
