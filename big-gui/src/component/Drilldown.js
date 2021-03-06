import React from 'react';
import { Button, Card, CardBody } from 'reactstrap';
import './Drilldown.css'
import loading_cat from './loading-cat.png'
import {GoogleCharts} from "google-charts";
import * as model from '../model/Model';

class Drilldown extends React.Component {
    constructor(props){
        super(props);
        this.deleteFrom = this.deleteFrom.bind(this);
        this.drawChart = this.drawChart.bind(this);
    }

    deleteFrom() {
        model.deleteDrilldownsFrom(this.props.drilldown);
    }

    componentDidMount() {
        this.props.drilldown.callback = this.drawChart;
    }

    render() {
        return (
            <div>
                <Card>
                <h3>This is a drilldown, id={this.props.id}</h3>
                    <CardBody>
                        <div id={"drilldown_" + this.props.measurement + "_" + this.props.id} className="drilldown">
                            <img src={loading_cat}/>

                        </div>
                        <Button onClick={this.deleteFrom}>Delete</Button>
                    </CardBody>
                </Card>
            </div>
        );
    }

    drawChart() {
        var drilldown = this.props.drilldown;
        var header = drilldown.header;
        var dataMatrix = drilldown.dataMatrix;
        var mix = [];
        mix.push(header);
        for(var i in dataMatrix) {
            mix.push(dataMatrix[i]);
        }
        var data = GoogleCharts.api.visualization.arrayToDataTable(mix);

        var options = {
            title: header.slice(1).join(",") + " by " + header[0]
        };

        var chart = this.createChart(document.getElementById("drilldown_" + this.props.measurement + "_" + this.props.id), drilldown.chartType);
        drilldown.chart = chart;
        chart.draw(data, options);

        GoogleCharts.api.visualization.events.addListener(chart, 'select', function() {
            var selection = drilldown.chart.getSelection();
            if (selection === undefined || selection.length == 0) {

                return;
            }
            var r = drilldown.dataMatrix[selection[0].row];
            // Get the modal
            var modal = document.getElementById('myModal' + drilldown.measurementId);

            // Get the <span> element that closes the modal
            // var span = document.getElementsByClassName("close)[0];

            modal.style.display = "block";

            // When the user clicks on <span> (x), close the modal
            /* span.onclick = function() {
                 modal.style.display = "none";
             }*/

            // When the user clicks anywhere outside of the modal, close it
            window.onclick = function (event) {
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            }

            var fixedDimensionAndValue = document.getElementsByClassName("fixedDimensionAndValue" + drilldown.measurementId)[0];
            fixedDimensionAndValue.innerHTML = header[0] + "=" + r[0];

        });
    }

    createChart(div, chartType) {
        if(chartType === "column") {
            return new GoogleCharts.api.visualization.ColumnChart(div);
        }

        if(chartType === "pie") {
            return new GoogleCharts.api.visualization.PieChart(div);
        }

        if(chartType === "timeseries") {
            return new GoogleCharts.api.visualization.LineChart(div);
        }
    }
}

export default Drilldown;