import apiRest from './utils/sgxApiRest';

const appInfoProvider = {
    loadInfo: () => {
        return apiRest.loadInfo();
    },
    getInfo: () => {
        return apiRest.getInfo();
    },
};

export default appInfoProvider;
