import React from 'react';
import {
    Datagrid,
    DateField,
    DeleteButton,
    Edit,
    EditButton,
    FormDataConsumer,
    ReferenceField,
    ReferenceInput,
    ReferenceManyField,
    required,
    SelectInput,
    SimpleForm,
    TextField,
    TextInput,
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";
import CustomToolbar from "../../modules/components/CustomToolbar";

const INTERNACION = 2;
const CUIDADOS_PROGRESIVOS = 2;

const SectorType = (sourceId) => {
    return (
        <ReferenceInput
            {...sourceId}
            reference="sectortypes"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>);

};


const AgeGroups = ({ formData, ...rest }) => {
    if (formData.sectorTypeId !== INTERNACION) return null;
    return ( <ReferenceInput
        {...rest}
        reference="agegroups"
        sort={{ field: 'description', order: 'ASC' }}
        perPage={10}
    >
        <SelectInput optionText="description" optionValue="id" />
    </ReferenceInput> );
};


const SectorOrganization = ({ formData, ...rest }) => {
    if (formData.sectorTypeId !== INTERNACION) return null;
    return (
        <ReferenceInput
            {...rest}
            reference="sectororganizations"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>);
};

const CareType = ({ formData, ...rest }) => {
    if (formData.sectorTypeId !== INTERNACION) return null;
    return (
        <ReferenceInput
            {...rest}
            reference="caretypes"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>);
};

const HospitalizationType = ({ formData, ...rest }) => {
    if (formData.sectorTypeId !== INTERNACION) return null;
    return (
        <ReferenceInput
            {...rest}
            reference="hospitalizationtypes"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>);
};

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

const validateCareType = (value, allValues) =>{
    if (allValues.sectorOrganizationId === CUIDADOS_PROGRESIVOS &&
        !allValues.careTypeId) {
        return 'El tipo de cuidado es requerido para esta organización de sector';
    }
    return [];
}

const CareTypeValidations = [validateCareType];

const SectorEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
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
                {formDataProps => ( <AgeGroups {...formDataProps} source="ageGroupId"/>)}
            </FormDataConsumer>
            {/*Sector Organizations*/}
            <FormDataConsumer>
                {formDataProps => ( <SectorOrganization {...formDataProps} source="sectorOrganizationId"/>)}
            </FormDataConsumer>
            {/*Care Type*/}
            <FormDataConsumer>
                {formDataProps => ( <CareType {...formDataProps} source="careTypeId" validate={CareTypeValidations}/>)}
            </FormDataConsumer>
            {/*Hospitalization Type*/}
            <FormDataConsumer>
                {formDataProps => ( <HospitalizationType {...formDataProps} source="hospitalizationTypeId"/>)}
            </FormDataConsumer>

            <SectionTitle label="resources.sectors.fields.clinicalspecialtysectors"/>
            <CreateRelatedButton
                reference="clinicalspecialtysectors"
                refFieldName="sectorId"
                label="resources.clinicalspecialtysectors.createRelated"
            />
            {/*TODO: Aislar esto en un componente. También se usa en show.js*/}
            <ReferenceManyField
                addLabel={false}
                reference="clinicalspecialtysectors"
                target="sectorId"
                sort={{ field: 'description', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
                    <TextField source="description" />
                    <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                        <TextField source="name" />
                    </ReferenceField>
                    <DeleteButton />
                </Datagrid>
            </ReferenceManyField>

            <SectionTitle label="resources.clinicalspecialtysectors.fields.rooms"/>
            <CreateRelatedButton
                reference="rooms"
                refFieldName="clinicalSpecialtySectorId"
                label="resources.rooms.createRelated"
            />
            <ReferenceManyField
                addLabel={false}
                reference="rooms"
                target="sectorId"
                sort={{ field: 'description', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
                    <TextField source="roomNumber" />
                    <TextField source="description"/>
                    <TextField source="type" />
                    <DateField source="dischargeDate" />
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>
        </SimpleForm>
    </Edit>
);

export default SectorEdit;
