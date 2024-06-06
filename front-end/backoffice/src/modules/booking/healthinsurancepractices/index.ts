import HealthInsurancePracticeShow from './show';
import HealthInsurancePracticeCreate from './create';
import HealthInsurancePracticeList from './list';
import HealthInsurancePracticeEdit from './edit';

const healthinsurancepractices = {
    show: HealthInsurancePracticeShow,
    create: HealthInsurancePracticeCreate,
    edit: HealthInsurancePracticeEdit,
    list: HealthInsurancePracticeList,
    options: {
        submenu: 'booking'
    }
};

export default healthinsurancepractices;
