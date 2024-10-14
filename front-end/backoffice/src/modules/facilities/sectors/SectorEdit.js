import React from 'react';
import {
    Datagrid,
    Edit,
    EditButton,
    DeleteButton,
    FormDataConsumer, ReferenceField,
    ReferenceInput,
    ReferenceManyField,
    required,
    SelectInput,
    SimpleForm,
    TextField,
    TextInput,
    BooleanInput,
    AutocompleteInput
} from 'react-admin';
import {
    SgxDateField,
    SgxSelectInput,
    CustomToolbar,
    SectionTitle,
} from '../../components';
import {
    CreateSector,
    CreateDoctorsOffice,
    CreateRooms,
    ShowServiceSectorData,
    CreateOrchestrator,
    UserIsAdmin,
    CreateEquipment,
    RenderModality,
    CreateShockroom,
} from './SectorShow';

const redirect = (basePath, id, data) => `/sectors/${data.id}/show`;

const INTERNACION = 2;
const DIAGNOSTICO_POR_IMAGENES = 4;

const SectorType = (sourceId) => {
    return (
        <ReferenceInput
            {...sourceId}
            reference="sectortypes"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" options={{ disabled: true }}/>
        </ReferenceInput>);

};

const HospitalizationField = ({formData, ...rest}) => {
    return formData.sectorTypeId !== INTERNACION ? null : (
        <ReferenceInput {...rest} sort={{ field: 'description', order: 'ASC' }}>
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>
    )
}

const CheckBoxField = ({formData}) => {
    return formData.sectorTypeId !== DIAGNOSTICO_POR_IMAGENES ? null : (
        <BooleanInput  label="resources.sectors.fields.informer" source="informer" />
    )
}

const Sector = ({ formData, ...rest }) => {
    return (
        <ReferenceInput
            {...rest}
            reference="sectors"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
            filter={{institutionId: formData.institutionId}}
        >
            <AutocompleteInput optionText="description" optionValue="id" />
        </ReferenceInput>);
};

const SectorEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect={ redirect } toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="description" validate={[required()]} />

            <SgxSelectInput source="institutionId" element="institutions" optionText="name" alwaysOn allowEmpty={false}/>

            {/*Parent Sector*/}
            <FormDataConsumer>
                {formDataProps => (<Sector {...formDataProps} source="sectorId"/>)}
            </FormDataConsumer>
            {/*Sector Type*/}
            <SectorType source="sectorTypeId"/>

            {/*Informer Sector*/}
            <FormDataConsumer>
              {formDataProps => (<CheckBoxField{... formDataProps} source = "informer"/>)}
            </FormDataConsumer>
            {/*Age Groups*/}
            <FormDataConsumer>
                {formDataProps => ( <HospitalizationField {...formDataProps} reference="agegroups" source="ageGroupId"/>)}
            </FormDataConsumer>
            {/*Sector Organizations*/}
            <FormDataConsumer >
                {formDataProps => ( <HospitalizationField {...formDataProps} reference="sectororganizations" source="sectorOrganizationId" />)}
            </FormDataConsumer>
            {/*Care Type*/}
            <FormDataConsumer >
                {formDataProps => ( <HospitalizationField {...formDataProps} reference="caretypes" source="careTypeId" />)}
            </FormDataConsumer>
            {/*Hospitalization Type*/}
            <FormDataConsumer>
                {formDataProps => ( <HospitalizationField {...formDataProps} reference="hospitalizationtypes" source="hospitalizationTypeId"/>)}
            </FormDataConsumer>

            <SectionTitle label="resources.sectors.fields.childSectors" />
            <CreateSector />
            <ReferenceManyField
                addLabel={false}
                reference="sectors"
                target= { "sectorId" }
                sort={{ field: 'description', order: 'DESC' }}
                perPage={100}
            >
                <Datagrid rowClick="show">
                    <TextField source="description" />
                    <ReferenceField source="sectorTypeId"  link={false}  reference="sectortypes">
                        <TextField source="description" />
                    </ReferenceField>
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>

            <SectionTitle label="resources.clinicalspecialtysectors.fields.doctorsoffices"/>
            <CreateDoctorsOffice />
            <ReferenceManyField
                addLabel={false}
                reference="doctorsoffices"
                target="sectorId"
                sort={{ field: 'description', order: 'DESC' }}
                filter={{ deleted: false }}
                perPage={100}
            >
                <Datagrid rowClick="show"
                          empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}} >Sin consultorios definidos</p>}>
                    <TextField source="description"/>
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>
            <CreateOrchestrator />
            <ReferenceManyField
                id='orchestrator'
                addLabel={false}
                reference="orchestrator"
                target="sectorId"
                sort={{ field: 'name', order: 'DESC' }}
                perPage={100}
                >
                <Datagrid rowClick={UserIsAdmin()?"show":""}>
                    <TextField source="name"/>
                    <TextField source="baseTopic"/>
                    <EditButton disabled= {!UserIsAdmin()}/>
                    <DeleteButton disabled= {!UserIsAdmin()}/>
                </Datagrid>
            </ReferenceManyField>

            <CreateEquipment />
            <ReferenceManyField
                id='equipment'
                addLabel={false}
                reference="equipment"
                target="sectorId"
                sort={{ field: 'aeTitle', order: 'DESC' }}
                perPage={100}
            >
                <Datagrid rowClick={UserIsAdmin()?"show":""}>
                    <TextField source="name" />
                    <TextField source="aeTitle" />
                    <ReferenceField link={false} source="pacServerId"  reference="pacserversimagelvl">
                        <TextField  source="name" />
                    </ReferenceField>
                    <ReferenceField link={false}source="orchestratorId" reference="orchestrator">
                        <TextField  source="name" />
                    </ReferenceField>
                    <ReferenceField link={false}source="modalityId" reference="modality">
                        <RenderModality/>
                    </ReferenceField>
                        <EditButton disabled= {!UserIsAdmin()}/>
                        <DeleteButton disabled= {!UserIsAdmin()}/>
                </Datagrid>
            </ReferenceManyField>

            <ShowServiceSectorData />

            {/*Rooms*/}
            <SectionTitle label="resources.clinicalspecialtysectors.fields.rooms"/>
            <CreateRooms />
            <ReferenceManyField
                addLabel={false}
                reference="rooms"
                target="sectorId"
                sort={{ field: 'description', order: 'DESC' }}
                perPage={100}
            >
                <Datagrid rowClick="show"
                          empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}} >Sin habitaciones definidas</p>}>
                    <TextField source="roomNumber" />
                    <TextField source="description"/>
                    <TextField source="type" />
                    <SgxDateField source="dischargeDate" />
                    <EditButton/>
                </Datagrid>
            </ReferenceManyField>

            <CreateShockroom/>
            <ReferenceManyField
                id='shockroom'
                addLabel={false}
                reference="shockroom"
                target="sectorId"
                sort={{ field: 'description', order: 'DESC' }}
                filter={{ deleted: false }}
                perPage={100}
            >
                <Datagrid rowClick="show"
                          empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin Shockrooms definidos</p>}>
                    <TextField source="description"/>
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>
        </SimpleForm>
    </Edit>
);

export default SectorEdit;
