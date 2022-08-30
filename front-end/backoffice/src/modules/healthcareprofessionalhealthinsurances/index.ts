import HealthcareProfessionalHealthInsuranceShow from "./show";
import HealthcareProfessionalHealthInsuranceCreate from "./create";
import HealthcareProfessionalHealthInsuranceList from "./list";
import HealthcareProfessionalHealthInsuranceEdit from "./edit";


const healthcareprofessionalhealthinsurances = {
    show: HealthcareProfessionalHealthInsuranceShow,
    create: HealthcareProfessionalHealthInsuranceCreate,
    list: HealthcareProfessionalHealthInsuranceList,
    edit: HealthcareProfessionalHealthInsuranceEdit,
    options: {
        submenu: 'booking'
    }
};

export default healthcareprofessionalhealthinsurances;
