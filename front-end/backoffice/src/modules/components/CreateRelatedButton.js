import React from 'react';
import {
    Link,
    useTranslate,
} from 'react-admin';
import Button from '@material-ui/core/Button';

//Taken from examples/simple/../posts/show.js
//This button takes the user to the create form of given resource.
//reference: The resource to be created.
//refFieldName: The field of the child entity that points
//to the father. This field will be prefilled in the create form.
//label: The button's label.
const CreateRelatedButton = ({ record, reference, refFieldName, label}) => {
    const newRecordValues = {[refFieldName]: record.id};
    const translate = useTranslate();
    return (
        <Button
        component={Link}
        to={{
            pathname: `/${reference}/create`,
            state: { record: newRecordValues },
        }}
        >{translate(label)}</Button>
    );
};

export default CreateRelatedButton;
