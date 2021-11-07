import Environment from './Environment';
import axios from 'axios';
import SingletonPromise from '@utils/SingletonPromise';

const DEFAULT_HEADERS = {
    Accept: 'application/json',
    'Content-Type': 'application/json',
};

const METHOD = {
    GET: 'get',
    POST: 'post',
};

module.exports = {
    METHOD,
    token: '',

    setAccessToken(token) {
        this.token = token;
    },

    getAccessToken() {
        return this.token;
    },

    _getHeader(headers) {
        headers = headers ? headers : { Authorization: `Bearer ${this.getAccessToken()}` };
        return {
            ...DEFAULT_HEADERS,
            ...headers,
        };
    },

    request(props) {
        const {
            path = '',
            data = null,
            method = METHOD.GET,
            requestId = new Date().getTime() + path,
            timeout = 30 * 1000, //in seconds
            retry = 0,
            headers,
        } = props;

        return SingletonPromise(requestId, (resolve, reject) => {
            const options = {
                method,
                timeout,
                url: Environment.getApiDomain() + path,
                headers: this._getHeader(headers),
                ...(!!data ? { data } : {}), // eleminate field data when undefined
            };

            const _successHandler = ({ data }) => {
                if (__DEV__) {
                    console.logg?.(data, 'green', '[RESPONSE from API]' + path);
                }
                if (data?.data?.accessToken) {
                    this.setAccessToken(data?.data?.accessToken);
                }
                resolve(data);
            };

            const _failHandler = e => {
                if (retry < 1) {
                    reject(e?.response?.data);
                    return;
                }

                this.request({
                    ...props,
                    retry: retry - 1,
                })
                    .then(resolve)
                    .catch(reject);
            };

            axios(options).then(_successHandler).catch(_failHandler);

            if (__DEV__) {
                console.logg?.(options, 'blue', '>>>>>> REQUEST API <<<<<<');
            }
        });
    },
};
