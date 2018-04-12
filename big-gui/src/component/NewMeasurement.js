import React from 'react';
import * as action from '../model/action.js';
import * as model from '../model/Model.js';
import './NewMeasurement.css'
import Filter from './Filter.js'
import { Button, Form, FormGroup, Label, Input, FormText } from 'reactstrap';
import {connect} from 'react-redux'
import {loadKpisForDataSource, reloadDataSources, loadDimensionsForDataSource} from '../actions/metadata-actions'

class NewMeasurement extends React.Component {
    constructor(props) {
        super(props);
        this.onUpdateDataSources = this.onUpdateDataSources.bind(this);
        this.onDataSourceChanged = this.onDataSourceChanged.bind(this);
    }

    onUpdateDataSources() {
        this.props.onUpdateDataSources();
    }

    onDataSourceChanged(event) {
        this.props.onUpdateKpiList(event.target.value);
        this.props.onUpdateDimensionList(event.target.value);
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
                    <Input type="select" id="datasourceSelector" onChange={this.onDataSourceChanged}>
                        {
                            this.props.metadata.datasources.map((dataSource) => (
                                <option value={dataSource.name}>{dataSource.displayName}</option>
                            ))
                        }
                    </Input>
                    <Label>Select kpis</Label>
                    <Input type="select" id="kpisSelector" multiple>
                        {
                            this.props.metadata.kpis && this.props.metadata.kpis.map((kpi) => (
                                <option value={kpi.name + "###" + kpi.offeredMetric}>{kpi.displayName}</option>
                            ))
                        }
                    </Input>
                    <Input type="select" className="dimensionsSelector" multiple>
                        {
                            this.props.metadata.dimensions && this.props.metadata.dimensions.map((dimension) => (
                                <option value={dimension.name}>{dimension.displayName}</option>
                            ))
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
}

const mapStateToProps = (state, props) => {
    return {
        metadata: state.metadata
    }
}

const mapActionsToProps = {
    onUpdateDataSources: reloadDataSources,
    onUpdateKpiList: loadKpisForDataSource,
    onUpdateDimensionList: loadDimensionsForDataSource
}

export default connect(mapStateToProps, mapActionsToProps)(NewMeasurement);