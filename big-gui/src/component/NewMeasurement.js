import React from 'react';
import * as appstate from '../appstate.js';
import './NewMeasurement.css'
import Filter from './Filter.js'

class NewMeasurement extends React.Component {
    componentDidMount() {
        appstate.loadDataSources();
    }

    addNewFilter() {
        var dim = appstate.getSelectValues(document.getElementsByClassName("dimensionFilterAdderSelector")[0])[0];
        appstate.measurementFilters.push({'dimension': dim});
        appstate.notify();
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
                <p>Filters</p>
                {
                    appstate.measurementFilters.map((filter) => (
                        <Filter dimension={filter.dimension}/>
                    ))
                }
                <p>Add new filter</p>
                <select className="dimensionFilterAdderSelector">
                    <option value="plate">Plate<input type="text"/></option>
                </select>
                <button onClick={this.addNewFilter}>Add...</button>
                <p></p>
                <button id="createMeasurement" onClick={appstate.addNewMeasurement}>New measurement</button>
            </div>
        );
    }
}

export default NewMeasurement;