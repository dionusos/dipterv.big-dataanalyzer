import React from 'react';
import './Filter.css'
import * as model from '../../model/Model.js';

class Filter extends React.Component {
    constructor(props) {
        super(props);
        this.removeIt = this.removeIt.bind(this)
    }

    removeIt() {
        model.removeFilterFromNewMeasurement(this.props.dimension);
    }

    render() {
        return (
            <div className="measurementFilter">
                <div className="dimensionName">{this.props.dimension}</div>=<input className="dimensionValue" type="text"/>
                <button onClick={this.removeIt}>Remove</button>
            </div>

        );
    }
}

export default Filter;
