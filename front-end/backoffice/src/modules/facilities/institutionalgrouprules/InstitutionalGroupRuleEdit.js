import React from 'react';
import {
    Edit,
    SimpleForm,
    TextInput,
    BooleanInput,
    TextField,
    useGetOne
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const InstitutionalGroupRuleEdit = (props) => {

    const goBack = () => {
        window.history.back();
    }

    const CustomBooleanInput = ({ record, ...props }) => {
        const isDisabled = record && record.ruleLevel === 'Local';
        return <BooleanInput {...props} disabled={isDisabled} />;
    };

    const institutionalgrouprule = useGetOne('institutionalgrouprules', props.id);
    const canDelete = institutionalgrouprule?.data && institutionalgrouprule.data?.ruleLevel === 'Local';
    const institutionalGroupId = institutionalgrouprule?.data?.institutionalGroupId;

    return (
        <Edit {...props}>
            <SimpleForm redirect={goBack} toolbar={<CustomToolbar isEdit={canDelete} deleteRedirect={`/institutionalgroups/${institutionalGroupId}/show/2`}/>} >
                <TextField label="Especialidad / Práctica o procedimiento" source="ruleName"  disabled={true}/>
                <TextField label="Nivel" source="ruleLevel" disabled={true}/>
                <CustomBooleanInput label="Regulación" source="regulated" />
                <TextInput label="Comentario" source="comment"/>
            </SimpleForm>
        </Edit>
    );
};

export default InstitutionalGroupRuleEdit;