import React from 'react';
import {
    BooleanField,
    Datagrid,
    FunctionField,
    Pagination,
    ReferenceField,
    ReferenceManyField,
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";

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
                    <SgxDateField source="entryDate" />
                </Datagrid>
            </ReferenceManyField>
        </SimpleShowLayout>
    </Show>
);

export default BedShow;
