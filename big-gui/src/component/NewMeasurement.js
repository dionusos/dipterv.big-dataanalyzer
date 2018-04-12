import React from 'react';
import * as action from '../model/action.js';
import * as model from '../model/Model.js';
import './NewMeasurement.css'
import Filter from './Filter.js'
import { Button, Form, FormGroup, Label, Input, FormText } from 'reactstrap';
import {connect} from 'react-redux'
import { reloadDataSources } from '../actions/metadata-actions'

class NewMeasurement extends React.Component {
    constructor (props){
        super(props);
        /*this.state = {
            datasourceList: [],
            kpis: [],
            dimensions: []
        }*/
        this.datasourceSelected = this.datasourceSelected.bind(this);
        this.onUpdateDataSources = this.onUpdateDataSources.bind(this);
    }

    onUpdateDataSources() {
        this.props.onUpdateDataSources();
    }

    addNewFilter() {
        var dim = model.getSelectValues(document.getElementsByClassName("dimensionFilterAdderSelector")[0])[0];
        model.measurementFilters.push({'dimension': dim});
        //model.notify();
    }

    render() {
        return (
            <div id="addNewMeasurement">
                <FormGroup>
                    <Label>Select datastore</Label>
                    <Input type="select" id="datasourceSelector" >
                        {
                            this.props.metadata.datasources.map((dataSource) => (
                                <option value={dataSource.name}>{dataSource.displayName}</option>
                            ))
                        }
                    </Input>
                    <Button onClick={this.onUpdateDataSources}>Refresh</Button>
                    <Label>Select kpis</Label>
                    <Input type="select" id="kpisSelector" multiple>
                        {
                            /*this.state.kpis.map((kpi) => (
                                <option value={kpi.name + "###" + kpi.offeredMetric}>{kpi.displayName}</option>
                            ))*/
                        }
                    </Input>
                    <Input type="select" className="dimensionsSelector" multiple>
                        {
                            /*this.state.dimensions.map((dimension) => (
                                <option value={dimension.name}>{dimension.displayName}</option>
                            ))*/
                        }
                    </Input>
                    <Label>Select date</Label>
                    <Input type="date" name="starDate"/>
                    <Input type="date" name="endDate"/>
                    <p>Filters</p>
                    {
                        model.measurementFilters.map((filter) => (
                            <Filter dimension={filter.dimension}/>
                        ))
                    }
                    <Label>Add new filter</Label>
                    <Input type="select" className="dimensionFilterAdderSelector">
                        {
                            /*this.state.dimensions.map((dimension) => (
                                <option value={dimension.name}>{dimension.displayName}</option>
                            ))*/
                        }
                    </Input>
                    <button id={"noId"} onClick={this.addNewFilter}>Add...</button>
                    <p></p>
                    <Button id="createMeasurement" onClick={this.handleCreateNewMeasurement}>New measurement</Button>
                </FormGroup>
            </div>
        );
    }

    handleCreateNewMeasurement() {
        model.addNewMeasurement();
    }

    datasourceSelected() {
        var obj = this;
        var selectBox = document.getElementById("datasourceSelector");
        if(selectBox === null) {
            return;
        }
        var selectedValue = selectBox.options[selectBox.selectedIndex].value;
        model.selectDatasource(selectedValue);

        if(selectedValue in model.dataStoreToKpis) {
            var newState = obj.state;
            newState.kpis = model.dataStoreToKpis[selectedValue];
            newState.dimensions = model.dataStoreToDimensions[selectedValue];
            obj.setState(newState);
            return;
        }

        var xmlHttp = new XMLHttpRequest();
        xmlHttp.onreadystatechange = function() {
            if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
                var result = JSON.parse(xmlHttp.responseText);
                var newState = obj.state;
                model.dataStoreToKpis[selectedValue] = result;
                newState.kpis = result;
                obj.setState(newState);
            }
        }
        xmlHttp.open( "GET", model.backend + "/metadata/datasource/" + selectedValue + "/kpi/list", true );
        xmlHttp.send( null );

        var xmlHttpD = new XMLHttpRequest();
        xmlHttpD.onreadystatechange = function() {
            if (xmlHttpD.readyState == 4 && xmlHttpD.status == 200) {
                var result = JSON.parse(xmlHttpD.responseText);
                var newState = obj.state;
                model.dataStoreToDimensions[selectedValue] = result;
                newState.dimensions = result;
                obj.setState(newState);
            }
        }
        xmlHttpD.open( "GET", model.backend + "/metadata/datasource/" + selectedValue + "/dimension/list", true );
        xmlHttpD.send( null );
        return xmlHttp.responseText;
    }
}

const mapStateToProps = (state, props) => {
    return {
        metadata: state.metadata
    }
}

const mapActionsToProps = {
    onUpdateDataSources: reloadDataSources
}

export default connect(mapStateToProps, mapActionsToProps)(NewMeasurement);