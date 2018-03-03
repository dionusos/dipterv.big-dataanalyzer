import React from 'react';
import * as appstate from '../appstate.js';
import './Filter.css'

class Filter extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="measurementFilter">
                <div className="dimensionName">{this.props.dimension}</div>=<input className="dimensionValue" type="text"/>
            </div>

        );
    }
}

export default Filter;
