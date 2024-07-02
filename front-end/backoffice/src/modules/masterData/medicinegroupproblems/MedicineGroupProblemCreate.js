import React from "react";
import { AutocompleteInput, Create, ReferenceInput, SimpleForm } from "react-admin";
import CustomToolbar from '../../components/CustomToolbar';


const MedicineGroupProblemCreate = (props) => {

    const redirect = `/medicinegroups/${props?.location?.state?.record?.medicineGroupId}/show/1`;

    return(
        <Create {...props}>

            <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
                
                <ReferenceInput
                    source="medicineGroupId"
                    reference="medicinegroups"
                    label="Nombre del grupo"
                >
                    <AutocompleteInput optionText="name" optionValue="id" disabled={true}></AutocompleteInput>
                </ReferenceInput>

                <ReferenceInput
                    source="problemId"
                    reference="medicinegroupproblems"
                    label="Problema/Diagnóstico"
                    filterToQuery={searchText => ({ conceptPt: searchText})}
                    sort={{ field: 'conceptPt', order: 'ASC'}}
                    perPage={1000}
                >
                    <AutocompleteInput optionText="conceptPt" optionValue="id"></AutocompleteInput>
                </ReferenceInput>

            </SimpleForm>

        </Create> 
    );
}

export default MedicineGroupProblemCreate;