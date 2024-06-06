import React, { useState, useEffect } from 'react';
import { TextInput, Create, SimpleForm, RadioButtonGroupInput, required, AutocompleteInput, ReferenceInput } from 'react-admin';

import CustomToolbar from '../../components/CustomToolbar';

const InstitutionalGroupRuleCreate = (props) => {

  const redirect=`/institutionalgroups/${props?.location?.state?.record?.institutionalGroupId}/show/2`;

  const [selectedSourceType, setSelectedSourceType] = useState(null);

  const onChange = (newValue) => {
    setSelectedSourceType(newValue);
  };

  useEffect(() => {
    if (props.record && props.record.sourceType) {
      setSelectedSourceType(props.record.sourceType);
    }
  }, [props.record]);

  return (
    <Create {...props}>
      <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
        <RadioButtonGroupInput
            source='Tipo de regla'
            choices={[
                { id: 'clinicalSpecialty', name: 'Especialidad' },
                { id: 'procedure', name: 'Practica/Procedimiento' },
            ]}
            validate={required()}
            onChange={onChange}
        />

        {selectedSourceType === 'clinicalSpecialty' && (
            <ReferenceInput
                label = "Especialidad"
                source = "clinicalSpecialtyId"
                reference = "clinicalspecialtyrules"
                sort={{ field: 'name', order: 'ASC' }}
                filterToQuery={searchText => ({name: searchText ? searchText : -1})}
                            >
                <AutocompleteInput optionText="name" optionValue="id" validate={[required()]} />
            </ReferenceInput>
        )}

        {selectedSourceType === 'procedure' && (
            <ReferenceInput
                label="Practica/Procedimiento"
                source="snomedId"
                reference="snomedprocedurerules"
                sort={{ field: 'conceptPt', order: 'ASC' }}
                filterToQuery={searchText => ({ conceptPt: searchText })}
            >
                <AutocompleteInput optionText="conceptPt" optionValue="id" validate={[required()]} resettable />
            </ReferenceInput>
        )}
        <TextInput source="comment" label="Comentario" />
      </SimpleForm>
    </Create>
  );
};

export default InstitutionalGroupRuleCreate;