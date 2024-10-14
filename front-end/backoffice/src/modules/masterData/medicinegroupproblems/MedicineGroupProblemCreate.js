import React from "react";
import { AutocompleteInput, Create, ReferenceInput, SimpleForm } from "react-admin";
import CustomToolbar from '../../components/CustomToolbar';

const goBack = () => {
    window.history.back();
}

const MedicineGroupProblemCreate = (props) => {
    return(
        <Create {...props}>

            <SimpleForm redirect={goBack} toolbar={<CustomToolbar />}>
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
                    perPage={100}
                >
                    <AutocompleteInput optionText="conceptPt" optionValue="id"></AutocompleteInput>
                </ReferenceInput>

            </SimpleForm>

        </Create> 
    );
}

export default MedicineGroupProblemCreate;