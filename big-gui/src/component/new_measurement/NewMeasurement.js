import React from 'react';
import './NewMeasurement.css'
import { Button, FormGroup, Label, Input } from 'reactstrap';
import {connect} from 'react-redux'
import {loadKpisForDataSource, reloadDataSources, loadDimensionsForDataSource, updateSelectedKpis, updateSelectedDimensions, updateSelectedDatasource, createMeasurement} from '../../actions/metadata-actions'
import {loadData} from "../../actions/data-actions";
import {LIMIT_DEFAULT} from "../../config/config";
import {dataQueryFromDrilldown} from "../../util/util";

class NewMeasurement extends React.Component {
    generateId() {
        return new Date();
    }

    constructor(props) {
        super(props);
        this.onUpdateDataSources = this.onUpdateDataSources.bind(this);
        this.onDataSourceChanged = this.onDataSourceChanged.bind(this);
        this.onKpiSelected = this.onKpiSelected.bind(this);
        this.onDimensionSelected = this.onDimensionSelected.bind(this);
        this.onCreateMeasurementPushed = this.onCreateMeasurementPushed.bind(this);
    }

    onUpdateDataSources() {
        this.props.onUpdateDataSources();
    }

    onDataSourceChanged(event) {
        this.props.onUpdateKpiList(event.target.value);
        this.props.onUpdateDimensionList(event.target.value);
        this.props.onUpdateSelectedDatasource(event.target.value);
    }

    onKpiSelected(event) {
        this.props.onKpiSelected(this.determineSelected(event));
    }

    onDimensionSelected(event) {
        this.props.onDimensionSelected(this.determineSelected(event));
    }

    onCreateMeasurementPushed() {
        let measurement = {};
        measurement.id = this.generateId();
        measurement.kpis = [];
        measurement.filters = [];
        measurement.datasourceDimensions = this.props.metadata.dimensions;
        measurement.upToDate = true;
        for(var i =0; i < this.props.metadata.selectedKpis.length; ++i){
            var k = this.props.metadata.selectedKpis[i].split("###");
            var k2 = {};
            k2.name = k[0];
            k2.offeredMetric = k[1];
            measurement.kpis.push(k2);
        }
        measurement.drilldowns = [];
        let drilldown = {};
        drilldown.id = {measurementId: measurement.id, id: "0"};
        drilldown.measurement = measurement;
        drilldown.limit = LIMIT_DEFAULT;
        drilldown.dimensions = [];
        drilldown.chartType = "column";
        drilldown.filters = [];
        for(var j =0; j < this.props.metadata.selectedDimensions.length; ++j){
            drilldown.dimensions.push({name: this.props.metadata.selectedDimensions[j]});
        }


        measurement.datasource = this.props.metadata.activeDataSource;
        measurement.drilldowns.push(drilldown);

        this.props.onCreateMeasurement(measurement);
        this.props.onLoadMeasurementData(dataQueryFromDrilldown(drilldown));
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
                    <Input type="select" id="kpisSelector"  onChange={this.onKpiSelected} multiple>
                        {
                            this.props.metadata.kpis && this.props.metadata.kpis.map((kpi) => (
                                <option value={kpi.name + "###" + kpi.offeredMetric}>{kpi.displayName}</option>
                            ))
                        }
                    </Input>
                    <Input type="select" className="dimensionsSelector"  onChange={this.onDimensionSelected} multiple>
                        {
                            this.props.metadata.dimensions && this.props.metadata.dimensions.map((dimension) => (
                                <option value={dimension.name}>{dimension.displayName}</option>
                            ))
                        }
                    </Input>
                    <Label>Select date</Label>
                    <Input type="date" name="starDate"/>
                    <Input type="date" name="endDate"/>
                    <p></p>
                    <Button id="createMeasurement" onClick={this.onCreateMeasurementPushed}>New measurement</Button>
                </FormGroup>
            </div>
        );
    }

    determineSelected(event) {
        var selected = [];
        for (var i = 0; i < event.target.options.length; ++i) {
            if (event.target.options[i].selected) {
                selected.push(event.target.options[i].value);
            }
        }
        return selected;
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
    onUpdateDimensionList: loadDimensionsForDataSource,
    onKpiSelected: updateSelectedKpis,
    onDimensionSelected: updateSelectedDimensions,
    onUpdateSelectedDatasource: updateSelectedDatasource,
    onCreateMeasurement: createMeasurement,
    onLoadMeasurementData: loadData
}


export default connect(mapStateToProps, mapActionsToProps)(NewMeasurement);