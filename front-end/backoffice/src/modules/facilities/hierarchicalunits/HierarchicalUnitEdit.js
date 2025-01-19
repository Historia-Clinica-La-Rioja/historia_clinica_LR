import React from 'react';
import {
    AutocompleteInput,
    Edit,
    FormDataConsumer,
    ReferenceInput,
    required, SelectInput,
    SimpleForm,
    TextInput
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import {
    HierarchicalUnitChilds,
    HierarchicalUnitParents,
    HierarchicalUnitStaff,
    HierarchicalUnitSectors,
} from './HierarchicalUnitShow';

const SERVICE = 8;

const searchToFilter = searchText => ({name: searchText ? searchText : -1});

const ServiceField = ({formData, ...rest}) => {
    return formData.typeId !== SERVICE ? null : (
        <ReferenceInput {...rest} sort={{ field: 'name', order: 'ASC' }} filterToQuery={searchToFilter}>
            <AutocompleteInput optionText="name" optionValue="id" validate={[required()]} />
        </ReferenceInput>
    )
}

const HierarchicalUnitParentsToReport = ({ record }) => {
    return (
        <ReferenceInput
            label='resources.hierarchicalunits.fields.hierarchicalUnitIdToReport'
            source="hierarchicalUnitIdToReport"
            reference="hierarchicalunits"
            sort={{ field: 'alias', order: 'ASC' }}
            filter={{institutionId: record.institutionId, id: record.id}}
        >
            <AutocompleteInput optionText="alias" optionValue="id" />
        </ReferenceInput>
    );
} 

const ClosestService = ({formData, record, ...rest}) => {

    return formData.typeId !== SERVICE ?
    (
        <ReferenceInput {...rest}
            label="resources.hierarchicalunits.fields.closestService"
            filter={{institutionId: record.institutionId, typeId: SERVICE}} >
            <AutocompleteInput allowEmpty resettable optionText="alias" optionValue="id"/>
        </ReferenceInput>
    ) : 
    null
}

const WarningClosestService = ({formData, record}) => {
    return formData.typeId !== SERVICE && record.typeId === SERVICE ? 
    (<p style={{paddingLeft:10, marginTop:0, color:'#ff9966', fontSize: 14}}>
        Atención: la edición del Tipo puede afectar a la configuración de Servicios Inmediatos Superiores. 
    </p>) : null;
}

const HierarchicalUnitEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <ReferenceInput
                 empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin Shockrooms definidos</p>}
                source="institutionId"
                reference="institutions"
                sort={{ field: 'name', order: 'ASC' }}
            >
                <AutocompleteInput optionText="name" optionValue="id" options={{ disabled: true }}/>
            </ReferenceInput>
            <TextInput source="alias" validate={[required()]} />
            <ReferenceInput
                reference="hierarchicalunittypes"
                source="typeId">
                <SelectInput optionText="description" optionValue="id" validate={[required()]}/>
            </ReferenceInput>
            <FormDataConsumer>
                {formDataProps => ( <ServiceField {...formDataProps} reference="clinicalservices" source="clinicalSpecialtyId"/>)}
            </FormDataConsumer>
            <FormDataConsumer>
                {formDataProps => ( <WarningClosestService {...formDataProps} source="closestServiceId"/>)}
            </FormDataConsumer>
            <FormDataConsumer>
                {formDataProps => ( <ClosestService {...formDataProps} reference="hierarchicalunits" source="closestServiceId"/>)}
            </FormDataConsumer>
            <HierarchicalUnitParentsToReport/>
            <HierarchicalUnitChilds/>
            <HierarchicalUnitParents/>
            <HierarchicalUnitStaff/>
            <HierarchicalUnitSectors/>
        </SimpleForm>
    </Edit>
    
);

export default HierarchicalUnitEdit;
