import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Frame from './component/Frame';
import registerServiceWorker from './registerServiceWorker';
import 'bootstrap/dist/css/bootstrap.css';
import { reloadDataSources } from './actions/metadata-actions'

import thunk from 'redux-thunk';
import {combineReducers, createStore, applyMiddleware, compose} from 'redux';
import {Provider} from 'react-redux';
import metadataReducer from './reducers/metadata-reducer';

const allReducers = combineReducers({
    metadata: metadataReducer
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
        }
     },
    allStoreEnhancers
);

ReactDOM.render(<Provider store={store}><Frame /></Provider>, document.getElementById('root'));
registerServiceWorker();

store.dispatch(reloadDataSources());
