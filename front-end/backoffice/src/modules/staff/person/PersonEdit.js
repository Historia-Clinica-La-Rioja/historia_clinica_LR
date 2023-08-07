import {
    Edit,
    SimpleForm,
    TextInput,
    required,
    maxLength,
    number,
    DateInput,
    minValue,
    maxValue
} from 'react-admin';
import CustomToolbar from "../../components/CustomToolbar";
import SgxSelectInput from "../../../sgxSelectInput/SgxSelectInput";

const formatDateToString = props => {
    const date = new Date();
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const value = `${date.getFullYear()}-${month}-${day}`;
    return value;
}   

const getMinValue = props => {
    return '1900-01-01';
}

const PersonEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={false}/>}>
            <TextInput source="firstName" validate={[required()]} />
            <TextInput source="middleNames" />
            <TextInput source="lastName" validate={[required()]} />
            <TextInput source="otherLastNames" />
            <SgxSelectInput source="identificationTypeId" element="identificationTypes" optionText="description" alwaysOn allowEmpty={false}/>
            <TextInput  source="identificationNumber" validate={[
                required(),
                number(),
                maxLength(11)]}/>
            <SgxSelectInput source="genderId" element="genders" optionText="description" alwaysOn allowEmpty={false}/>
            <DateInput source="birthDate" validate={[required(), minValue(getMinValue()), maxValue(formatDateToString())]} />
            <TextInput source="email" validate={[maxLength(100)]}/>
        </SimpleForm>
    </Edit>
);

export default PersonEdit;
