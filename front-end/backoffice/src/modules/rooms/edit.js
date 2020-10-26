import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
    DateInput,
    TextField,
    ReferenceField,
    ReferenceManyField,
    Datagrid,
    EditButton,
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";
import CustomToolbar from "../../modules/components/CustomToolbar";

const RoomEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="roomNumber" validate={[required()]} />
            <TextInput source="description" validate={[required()]} />
            <TextInput source="type" validate={[required()]} />
            <DateInput source="dischargeDate" />

            <SgxSelectInput label="Especialidad | Sector" source="clinicalSpecialtySectorId" element="clinicalspecialtysectors" optionText="description" alwaysOn allowEmpty={false}/>

            <SectionTitle label="resources.rooms.fields.beds"/>
            <CreateRelatedButton
                reference="beds"
                refFieldName="roomId"
                label="resources.beds.createRelated"
            />
            {/*TODO: Aislar esto en un componente. También se usa en show.js*/}
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

        </SimpleForm>
    </Edit>
);

export default RoomEdit;
