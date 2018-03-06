import React from 'react';
import * as appstate from '../appstate.js';
import './NewMeasurement.css'
import Filter from './Filter.js'
import { Button, Form, FormGroup, Label, Input, FormText } from 'reactstrap';

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
                    <FormGroup>
                    <Label>Select datastore</Label>
                    <Input type="select" id="datasourceSelector" onChange={appstate.datasourceSelected}>
                    </Input>
                    <Label>Select kpis</Label>
                    <Input type="select" id="kpisSelector" multiple>
                    </Input>
                    <Input type="select" className="dimensionsSelector" multiple>
                    </Input>
                    <Label>Select date</Label>
                    <Input type="date" name="starDate"/>
                    <Input type="date" name="endDate"/>
                    <p>Filters</p>
                    {
                        appstate.measurementFilters.map((filter) => (
                            <Filter dimension={filter.dimension}/>
                        ))
                    }
                    <Label>Add new filter</Label>
                    <Input type="select" className="dimensionFilterAdderSelector">
                    </Input>
                    <button id={"noId"} onClick={this.addNewFilter}>Add...</button>
                    <p></p>
                    </FormGroup>
                    <Button id="createMeasurement" onClick={appstate.addNewMeasurement}>New measurement</Button>
            </div>
        );
    }
}

export default NewMeasurement;