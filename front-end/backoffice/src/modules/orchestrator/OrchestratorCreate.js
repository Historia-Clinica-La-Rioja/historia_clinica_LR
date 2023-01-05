import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required,
    maxLength,
    AutocompleteInput,
    ReferenceInput
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";

const DIAGNOSTICO_POR_IMAGENES = 4;
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

                       </SimpleForm>
                   </Create>
               );
};



export default OrchestratorCreate;