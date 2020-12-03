import { loadInfo, getInfo} from './utils/sgxApiInfo';

const appInfoProvider = {
    loadInfo: () => {
        return loadInfo();
    },
    getInfo: () => {
        return getInfo();
    },
};

export default appInfoProvider;
