import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Frame from './component/frame/Frame';
import registerServiceWorker from './registerServiceWorker';
import 'bootstrap/dist/css/bootstrap.css';
import { reloadDataSources } from './actions/metadata-actions'

import thunk from 'redux-thunk';
import {combineReducers, createStore, applyMiddleware, compose} from 'redux';
import {Provider} from 'react-redux';
import metadataReducer from './reducers/metadata-reducer';
import dataReducer from "./reducers/data-reducer";
import {GoogleCharts} from 'google-charts';

GoogleCharts.load();


const allReducers = combineReducers({
    metadata: metadataReducer,
    data: dataReducer
});

const allStoreEnhancers = compose(
    applyMiddleware(thunk),
    window.devToolsExtension && window.devToolsExtension()
);

const store = createStore(
    allReducers,
    {
        metadata:{
            datasources: [],
            kpis: [],
            dimensions: []
        },
        data: {
            measurements: []
        }
     },
    allStoreEnhancers
);

ReactDOM.render(<Provider store={store}><Frame /></Provider>, document.getElementById('root'));
registerServiceWorker();

store.dispatch(reloadDataSources());
