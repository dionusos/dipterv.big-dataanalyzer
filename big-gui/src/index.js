import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Frame from './component/Frame';
import registerServiceWorker from './registerServiceWorker';

ReactDOM.render(<Frame />, document.getElementById('root'));
registerServiceWorker();
