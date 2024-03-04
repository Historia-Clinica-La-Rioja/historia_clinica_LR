import { 
    Create, 
    SimpleForm,
    ReferenceInput,
    AutocompleteInput,
    required,
 } from 'react-admin';

import CustomToolbar from '../../components/CustomToolbar';

const personData = (person) => {
    return person ? `${person.completeName ? person.completeName : "" } ${person.identificationNumber ? "- " + person.identificationNumber : "" }` : null;
};

const InstitutionalGroupUserCreate = (props) => {
    const redirect=`/institutionalgroups/${props?.location?.state?.record?.institutionalGroupId}/show/1`;
    return (
        <Create {...props}> 
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
                <ReferenceInput
                    source='userId'
                    reference='manageruserpersons'
                    label='Usuario'
                    filterToQuery={searchText => ({searchText: searchText ? searchText : ''})}
                >
                    <AutocompleteInput optionText={personData} optionValue="id" validate={[required()]} />
                </ReferenceInput>
            </SimpleForm>
        </Create>
    );
};

export default InstitutionalGroupUserCreate;