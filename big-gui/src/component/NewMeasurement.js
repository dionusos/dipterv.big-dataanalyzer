import React from 'react';
import * as appstate from '../appstate.js';

class NewMeasurement extends React.Component {
    componentDidMount() {
        appstate.loadDataSources();
    }

    render() {
        return (
            <div id="addNewMeasurement">
                <p>Select datastore</p>
                <select id="datasourceSelector" onChange={appstate.datasourceSelected}>
                </select>
                <p>Select kpis</p>
                <select id="kpisSelector" multiple>
                </select>
                <select className="dimensionsSelector" multiple>
                </select>
                <p>Select date</p>
                <input type="date" name="starDate"/>
                <input type="date" name="endDate"/>
                <button id="createMeasurement" onClick={appstate.addNewMeasurement}>New measurement</button>
            </div>
        );
    }
}

export default NewMeasurement;