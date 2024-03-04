import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required,
    maxLength,
    AutocompleteInput,
    ReferenceInput,
    NumberInput,
    regex,
} from 'react-admin';

import CustomToolbar from '../../components/CustomToolbar';
import SectionTitle from '../../components/SectionTitle';

const DIAGNOSTICO_POR_IMAGENES = 4;

const validateTime = regex(/^(?:[01]\d|2[0-3]):[0-5]\d:[0-5]\d$/, 'resources.orchestrator.errorTime');
const validateDecimal =regex(/^(0\.[0-9][0-9]?|1\.00)$/,'resources.orchestrator.errorDecimal');
const OrchestratorCreate = props => {

    return (
                   <Create {...props} >

                       <SimpleForm redirect="show" toolbar={<CustomToolbar/>}>

                           <TextInput source="name" validate={[
                               required(),
                               maxLength(40)]}
                           />

                           <TextInput source="baseTopic" validate={[
                               required(),
                               maxLength(250)]}
                           />
                           <ReferenceInput source="sectorId" reference="sectors"
                                validate={[required()]}
                                disabled={true}
                                filter ={{ sectorTypeId :DIAGNOSTICO_POR_IMAGENES}}
                           >
                               <AutocompleteInput optionText="description" optionValue="id"/>
                           </ReferenceInput>

                           <SectionTitle label="resources.orchestrator.parameter"/>

                           <NumberInput source="attempsNumber"  validate={[required()]} defaultValue="3"/>

                           <NumberInput source="numberToMove"  validate={[required()]}defaultValue="10"/>

                           <TextInput source="executionStartTime"  validate={[required(),validateTime]} defaultValue="08:00:00"/>

                           <TextInput source="executionEndTime"  validate={[required(),validateTime]} defaultValue="06:00:00"/>

                           <TextInput source="weightDays"   validate={[required(),validateDecimal]} defaultValue="0.3"/>

                           <TextInput source="weightSize" validate={[required(),validateDecimal]} defaultValue="0.01"/>

                           <TextInput source="weightPriority" validate={[required(),validateDecimal]}defaultValue="0.2"/>


                       </SimpleForm>
                   </Create>
               );
};



export default OrchestratorCreate;