import React from 'react';
import {Datagrid, EditButton, ReferenceField, ReferenceManyField, Show, SimpleShowLayout, TextField} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';
import SgxDateField from "../../dateComponents/sgxDateField";

const RoomShow = props => (
    <Show  {...props}>
        <SimpleShowLayout>
            <TextField source="roomNumber"/>
            <TextField source="description" />
            <TextField source="type" />
            <SgxDateField source="dischargeDate" />
            <ReferenceField source="sectorId" reference="sectors" link="show">
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
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>
        </SimpleShowLayout>
    </Show>
);

export default RoomShow;
