import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    FunctionField,
    BooleanField,
    ReferenceManyField,
    Datagrid,
    DateField,
    Pagination,
} from 'react-admin';

const renderRoom = room => `${room.roomNumber} - ${room.description}`;
const BedShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="bedNumber" />
            <ReferenceField source="roomId" reference="rooms">
                <FunctionField render={renderRoom} />
            </ReferenceField>
            <BooleanField source="enabled" />
            <BooleanField source="available" />
            <BooleanField source="free" />
            <ReferenceManyField
                addLabel={true}
                label="resources.beds.fields.internmentepisodes"
                reference="internmentepisodes"
                target="bedId"
                sort={{ field: 'entryDate', order: 'DESC' }}
                filter={{ statusId: 1 }}
                pagination={<Pagination />}
            >
                <Datagrid>
                    <DateField source="entryDate" />
                </Datagrid>
            </ReferenceManyField>
            <ReferenceField source="bedCategoryId" reference="bedcategories" >
                <TextField source="description" />
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default BedShow;
