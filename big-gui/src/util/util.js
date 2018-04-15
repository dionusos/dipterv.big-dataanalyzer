export function dataQueryFromDrilldown(drilldown) {
    let dataQuery = {};
    dataQuery.id = drilldown.id;
    dataQuery.datasource = drilldown.measurement.datasource;
    dataQuery.kpis = drilldown.measurement.kpis;
    dataQuery.dimensions = drilldown.dimensions;
    dataQuery.filters = [];
    dataQuery.limit = drilldown.limit;
    dataQuery.orders = [{kpi: drilldown.measurement.kpis[0], direction: "DESC"}];
    return dataQuery;
}