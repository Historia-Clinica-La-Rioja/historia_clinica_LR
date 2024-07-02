import React from "react";
import { AutocompleteInput, SimpleForm, Create, ReferenceInput } from "react-admin";
import CustomToolbar from '../../components/CustomToolbar';


const MedicineGroupMedicineCreate = (props) => {
    const redirect = `/medicinegroups/${props?.location?.state?.record?.medicineGroupId}/show`;

    return(
        <Create {...props}>

            <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
            
                <ReferenceInput
                    source="medicineGroupId"
                    reference="medicinegroups"
                    label="Nombre del grupo">
                    <AutocompleteInput optionText="name" optionValue="id" disabled={true}></AutocompleteInput>
                </ReferenceInput>
            
                <ReferenceInput
                    source="medicineId"
                    reference="medicinefinancingstatus"
                    label="Fármaco"
                    filter={{ financed: true}}
                    sort={{ field: 'name', order:'ASC' }}
                    perPage={1000}
                >
                    <AutocompleteInput optionText="conceptPt" optionValue="id"></AutocompleteInput>
                </ReferenceInput>
            
            </SimpleForm>
        
        </Create>
    );
} 

export default MedicineGroupMedicineCreate;