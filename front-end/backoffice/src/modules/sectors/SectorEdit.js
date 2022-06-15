import React from 'react';
import {
    Datagrid,
    Edit,
    EditButton,
    FormDataConsumer, ReferenceField,
    ReferenceInput,
    ReferenceManyField,
    required,
    SelectInput,
    SimpleForm,
    TextField,
    TextInput,
} from 'react-admin';
import SectionTitle from '../components/SectionTitle';
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";
import CustomToolbar from "../components/CustomToolbar";
import SgxDateField from "../../dateComponents/sgxDateField";
import { CreateSector, CreateDoctorsOffice, CreateRooms, ShowServiceSectorData } from './SectorShow';

const redirect = (basePath, id, data) => `/sectors/${data.id}/show`;

const INTERNACION = 2;

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

const Sector = ({ formData, ...rest }) => {
    return (
        <ReferenceInput
            {...rest}
            reference="sectors"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
            filter={{institutionId: formData.institutionId}}
        >
            <SelectInput optionText="description" optionValue="id" />
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
            >
                <Datagrid rowClick="show"
                          empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}} >Sin consultorios definidos</p>}>
                    <TextField source="description"/>
                    <EditButton />
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
                sort={{ field: 'description', order: 'DESC' }}>
                <Datagrid rowClick="show"
                          empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}} >Sin habitaciones definidas</p>}>
                    <TextField source="roomNumber" />
                    <TextField source="description"/>
                    <TextField source="type" />
                    <SgxDateField source="dischargeDate" />
                    <EditButton/>
                </Datagrid>
            </ReferenceManyField>
        </SimpleForm>
    </Edit>
);

export default SectorEdit;
