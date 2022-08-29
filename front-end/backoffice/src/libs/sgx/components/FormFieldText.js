import React from 'react';

import { Field } from 'react-final-form';
import { TextField } from '@material-ui/core';

const showErrorOnChange = ({
    meta: {
        submitError,
        dirtySinceLastSubmit,
        error,
        touched,
        modified
    },
}) => !!(((submitError && !dirtySinceLastSubmit) || error) && (touched || modified));

const TextFieldWrapper = ({
    input: { name, value, type, onChange, ...restInput },
    meta,
    required,
    fullWidth = true,
    variant = 'filled',
    helperText,
    showError = showErrorOnChange,
    ...rest
}) => {

    const { error, submitError } = meta;
    const isError = showError({ meta });

    return (
        <TextField
            fullWidth={fullWidth}
            helperText={isError ? error || submitError : helperText}
            error={isError}
            onChange={onChange}
            name={name}
            value={value}
            type={type}
            required={required}
            variant={variant}
            inputProps={{ required, ...restInput }}
            {...rest}
        />
    );
};

const FormFieldText = ({
    name,
    type,
    fieldProps,
    ...rest
}) => (
    <Field
        name={name}
        type={type}
        render={({ input, meta }) => (
            <TextFieldWrapper input={input} meta={meta} name={name} type={type} {...rest} />
        )}
        {...fieldProps}
    />
);

export default FormFieldText;
