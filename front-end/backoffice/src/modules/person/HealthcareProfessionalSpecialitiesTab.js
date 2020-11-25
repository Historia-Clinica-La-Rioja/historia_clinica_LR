import React, { useState, useEffect, Fragment } from 'react';
import {
    Loading,
    TextField,
    useDataProvider,
    useTranslate,
    useGetOne,
} from 'react-admin';
import {
    TableCell,
    TableBody,
    TableRow,
    TableHead,
    TableContainer,
    Table,
    Paper,
    Chip,
} from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import CreateRelatedButton from '../components/CreateRelatedButton';

const useStyles = makeStyles({
    chip: {
        display: 'flex',
        flexWrap: 'wrap',
    },
});

const HealthcareSpecialities = ({id, reference, source}) => {
    const { data, loading } = useGetOne(reference, id);

    return loading ? <Loading /> : <TextField record={data} source={source}/>;
};

const HealthcareProfessionalChip = ({healthcareProfessional}) => {
    const healthcareProfessionalsRef = `#/healthcareprofessionals/${healthcareProfessional.id}/edit`;
    return <Chip style={{margin: "10px 0px 5px 0px"}} color="primary" label={`Matrícula ${healthcareProfessional.licenseNumber}`} component="a" href={healthcareProfessionalsRef} clickable />
};

const HealthcareProfessionalSpecialitiesTab = ({ record }) => {
    const { id } = record;
    const translate = useTranslate();
    const dataProvider = useDataProvider();
    const classes = useStyles();

    const [healthcareProfessionals, setHealthcareProfessionals] = useState();
    const [loadingHealthcareProfessionals, setLoadingHealthcareProfessionals] = useState(true);
    const [errorHealthcareProfessionals, setErrorHealthcareProfessionals] = useState();

    const [healthcareProfessionalSpecialities, setHealthcareProfessionalSpecialities] = useState();
    const [loadingHealthcareProfessionalSpecialities, setLoadingHealthcareProfessionalSpecialities] = useState(true);
    const [errorHealthcareProfessionalSpecialities, setErrorHealthcareProfessionalSpecialities] = useState();

    useEffect(() => {
        dataProvider.getManyReference('healthcareprofessionals', {
            target: 'personId',
            id: id,
            sort: { field: 'personId', order: 'DESC' },
            pagination: { page: 1, perPage: 10 }
        })
            .then((response) => {
                const healthcareProfessionalsData = response ? response.data : undefined;
                setHealthcareProfessionals(healthcareProfessionalsData);
                setLoadingHealthcareProfessionals(false);

                if (healthcareProfessionalsData && healthcareProfessionalsData.length) {
                    dataProvider.getManyReference('healthcareprofessionalspecialties', {
                        target: 'healthcareProfessionalId',
                        id: healthcareProfessionalsData[0].id,
                        sort: { field: 'healthcareProfessionalId', order: 'DESC' },
                        pagination: { page: 1, perPage: 10 }  
                    }).then(({ data: healthcareProfessionalSpecialitiesData }) => {
                        setHealthcareProfessionalSpecialities(healthcareProfessionalSpecialitiesData);
                        setLoadingHealthcareProfessionalSpecialities(false);
                    }).catch(error => {
                        setErrorHealthcareProfessionalSpecialities(error);
                        setLoadingHealthcareProfessionalSpecialities(false);
                    })
                } else {
                    setLoadingHealthcareProfessionalSpecialities(false);
                }
            })
            .catch(error => {
                setErrorHealthcareProfessionals(error);
                setLoadingHealthcareProfessionals(false);
            })
    }, [id, dataProvider]);

    if (loadingHealthcareProfessionals || loadingHealthcareProfessionalSpecialities) return <Loading />;
    if (errorHealthcareProfessionals || errorHealthcareProfessionalSpecialities) return <span />;
    if (healthcareProfessionals && !healthcareProfessionals.length) return (<CreateRelatedButton
                                                    record={record}
                                                    reference="healthcareprofessionals"
                                                    refFieldName="personId"
                                                    label="resources.healthcareprofessionals.createRelated" />);

    if (!healthcareProfessionalSpecialities) return <Loading />;

    return (
            
        <Fragment>
        
            <div className={classes.chip}>
                {healthcareProfessionals.map(hp => <HealthcareProfessionalChip key={hp.id} healthcareProfessional={hp} />)}
            </div>

            <TableContainer component={Paper} style={{margin: "20px 0px"}}>
            <Table aria-label="simple table">
            <TableHead>
                <TableRow>
                    <TableCell align="center">{translate('resources.healthcareprofessionalspecialties.fields.professionalSpecialtyId')}</TableCell>
                    <TableCell align="center">{translate('resources.healthcareprofessionalspecialties.fields.clinicalSpecialtyId')}</TableCell>
                </TableRow>
            </TableHead>
            <TableBody>
                {healthcareProfessionalSpecialities.map(hps =>
                    <TableRow key={hps.id}>
                        <TableCell align="center">
                            <HealthcareSpecialities reference='professionalspecialties' id={hps.professionalSpecialtyId} source='description'/>
                        </TableCell>
                        <TableCell align="center">
                            <HealthcareSpecialities reference='clinicalspecialties' id={hps.clinicalSpecialtyId} source='name'/>
                        </TableCell>
                    </TableRow>
                )}
            </TableBody>
            </Table>
            </TableContainer>
        </Fragment>

    );
};

export default HealthcareProfessionalSpecialitiesTab;