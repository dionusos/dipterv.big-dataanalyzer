import React from 'react';
import * as appstate from '../appstate.js';

class NewMeasurement extends React.Component {
    render() {
        return (
            <div id="addNewMeasurement">
                <p>Select datastore</p>
                <select id="datasourceSelector">
                </select>
                <p>Select kpis</p>
                <select id="kpisSelector" multiple>
                </select>
                <select id="dimensionsSelector" multiple>
                </select>
                <p>Select date</p>
                <input type="date" name="starDate"/>
                <input type="date" name="endDate"/>
            </div>
        );
    }
}

export default NewMeasurement;