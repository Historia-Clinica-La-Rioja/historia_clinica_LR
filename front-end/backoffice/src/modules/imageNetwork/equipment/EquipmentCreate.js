import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required,
    maxLength,
    ReferenceInput,
    SelectInput,
    BooleanInput,
    AutocompleteInput
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import SectionTitle from '../../components/SectionTitle';

const DIAGNOSTICO_POR_IMAGENES = 4;
const EquipmentCreate = props=> {
    let filterValue =props.location.state?.record.sectorId;
    const renderModality = (choice) => choice ? `${choice.acronym} (${choice.description})` : '';

    return (
                   <Create {...props} >

                       <SimpleForm redirect="show" toolbar={<CustomToolbar/>}>
                           <SectionTitle label="resources.equipment.detailLabel"/>
                           <TextInput source="name" validate={[required(),maxLength(40)]}/>
                           <TextInput source="aeTitle" validate={[
                               required(),
                               maxLength(15)]}
                           />
                           <ReferenceInput source={"sectorId"} reference="sectors"
                                disabled={true}
                                validate={[required()]}
                                filter ={{ sectorTypeId :DIAGNOSTICO_POR_IMAGENES}}
                            >
                               <SelectInput optionText="description" optionValue="id"/>
                           </ReferenceInput>
                           <ReferenceInput source="pacServerId" reference="pacserversimagelvl"
                                validate={[required()]}
                                filter ={{ sectorId :filterValue}}
                           >
                                <SelectInput optionText="name" optionValue="id"/>
                           </ReferenceInput>
                           <ReferenceInput source="orchestratorId" reference="orchestrator"
                                validate={[required()]}
                                filter ={{ sectorId :filterValue }}
                           >
                                <SelectInput optionText="name" optionValue="id"/>
                           </ReferenceInput>
                           <ReferenceInput source="modalityId" reference="modality"
                                validate={[required()]}
                           >
                                <AutocompleteInput optionText={renderModality} optionValue="id" />
                           </ReferenceInput>

                           <BooleanInput label="resources.equipment.fields.createId" source="createId" />

                       </SimpleForm>


                   </Create>
               );
};



export default EquipmentCreate;