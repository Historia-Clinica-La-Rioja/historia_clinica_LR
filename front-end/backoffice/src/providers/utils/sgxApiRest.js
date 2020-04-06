import { fetchUtils } from 'react-admin';

class SgxApiRest {

    auth(username, password) {
        this.logout();
        const options = { 
            method: 'POST', 
            headers: new Headers({ Accept: 'application/json' }),
            body: JSON.stringify({username, password}) 
        };
        return fetchUtils.fetchJson('/api/auth', options)
            .then(response => {
                return response.json;
            }).then(({ token }) => {
                localStorage.setItem('token', token);
            });
    }

    hasToken() {
        const isTokenStored = !!localStorage.getItem('token');
        //console.log(`hasToken?`, isTokenStored);
        return isTokenStored;
    }
  
    fetch(url, options = {}) {
        if (!options.headers) {
            options.headers = new Headers({ Accept: 'application/json' });
        }
        const token = localStorage.getItem('token');
        options.headers.set('X-Auth-Token', token);
        return fetchUtils.fetchJson(url, options);
    };

    logout() {
        localStorage.clear();
    }

  }
  
  export default new SgxApiRest();