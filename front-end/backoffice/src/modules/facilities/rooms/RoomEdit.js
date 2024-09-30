import React from 'react';
import {
    Datagrid,
    Edit,
    EditButton,
    ReferenceInput,
    ReferenceManyField,
    required,
    SimpleForm,
    TextField,
    TextInput, 
    useGetOne, 
    usePermissions,
    AutocompleteInput
} from 'react-admin';
import {
    CustomToolbar,
    CreateRelatedButton,
    SectionTitle,
} from '../../components';
import SgxDateInput from '../../../dateComponents/sgxDateInput';

const INTERNACION = 2;

const SectorField = ({ record }) => {
    const sector = useGetOne('sectors',  record.sectorId );
        return sector.data ? (
            <ReferenceInput
                source="sectorId"
                reference="sectors"
                perPage={100}
                sort={{ field: 'description', order: 'ASC' }}
                filter={{sectorTypeId: INTERNACION, institutionId: sector.data.institutionId}}
            >
                <AutocompleteInput optionText="description" optionValue="id" />
            </ReferenceInput>
        ) : null;
}


const RoomEdit = props => {
    const { permissions } = usePermissions();
    return (
        <Edit {...props}>
            <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
                <TextInput source="roomNumber" validate={[required()]} />
                <TextInput source="description" validate={[required()]} />
                <TextInput source="type" validate={[required()]} />
                <SgxDateInput source="dischargeDate" />
                <SectorField/>


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
                        <EditButton />
                    </Datagrid>
                </ReferenceManyField>

                {permissions && permissions.isOn('HABILITAR_LLAMADO') && <TextInput source="topic" />}

            </SimpleForm>
        </Edit>
    )
};

export default RoomEdit;
