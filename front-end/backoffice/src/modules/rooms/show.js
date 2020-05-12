import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    DateField,
    ReferenceManyField,
    Datagrid,
    EditButton
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';

const RoomShow = props => (
    <Show  {...props}>
        <SimpleShowLayout>
            <TextField source="roomNumber"/>
            <TextField source="description" />
            <TextField source="type" />
            <DateField source="dischargeDate" />
            <ReferenceField source="clinicalSpecialtySectorId" reference="clinicalspecialtysectors" link="show">
                <TextField source="description"/>
            </ReferenceField>
            <SectionTitle label="resources.rooms.fields.beds"/>
            <CreateRelatedButton
                reference="beds"
                refFieldName="roomId"
                label="resources.beds.createRelated"
            />
            {/*TODO: Aislar esto en un componente. También se usa en edit.js*/}
            <ReferenceManyField
                addLabel={false}
                reference="beds"
                target="roomId"
                sort={{ field: 'bedNumber', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
                    <TextField source="bedNumber" />
                    <ReferenceField source="bedCategoryId" reference="bedcategories" >
                        <TextField source="description" />
                    </ReferenceField>
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>
        </SimpleShowLayout>
    </Show>
);

export default RoomShow;
