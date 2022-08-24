import * as React from "react";
import PropTypes from 'prop-types';
import { useRecordContext } from 'react-admin';

import Typography from '@material-ui/core/Typography';

const TypographyField = (props) => {
    const { source, variant = 'h2', formatter = (value) => value } = props;

    const record = useRecordContext(props);
    return <Typography variant={variant} gutterBottom>{formatter(record[source])}</Typography>;
}

TypographyField.propTypes = {
    label: PropTypes.string,
    record: PropTypes.object,
    source: PropTypes.string.isRequired,
    variant: PropTypes.string,
    formatter: PropTypes.func,
};

export default TypographyField;
