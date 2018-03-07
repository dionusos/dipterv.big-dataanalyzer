import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Frame from './component/Frame';
import registerServiceWorker from './registerServiceWorker';
import 'bootstrap/dist/css/bootstrap.css';

ReactDOM.render(<Frame />, document.getElementById('root'));
registerServiceWorker();
