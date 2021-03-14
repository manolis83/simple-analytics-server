# Simple Analytics

## Overview
The simple analytics server is a Spring Boot application implemented using Java. It exposes a REST-like
api that uses consumes JSON requests and produces JSON responses over HTTP.

## API
### Requests
The API consumes JSON requests that specify the metrics, dimension filters, and group-by dimensions.

e.g.
```json
{
  "metricSelections": [
    {
        "metricType": "campaign"
    },
    {
      "aggregateFunction": "sum",
      "metricType": "impressions"
    }
  ],
  "dimensionsFilters": [
    {
      "type": "equal",
      "dimension": "campaign",
      "value": "Campaign 1"
    },
    {
      "type": "greaterThan",
      "dimension": "date",
      "value": "11/30/19"
    }
  ],
  "groupBy":["campaign"]
}
```
### Responses
The responses are produced as JSON objects as well, containing a `data` object which contains the
requested data in case of a successful execution, and an `error` object which contains any error messages,
and is empty in case of a successful execution.

The response of a successful execution
```json
{
    "data": [
        {
            "campaign": "Campaign 1",
            "impressions": 10899
        }
    ],
    "error": null
}
```
A response reporting an erroneous query:
```json
{
    "data": null,
    "error": "The following identifiers are needed in the groupBy: campaign"
}
```
## Server setup
The analytics server is a self-contained application using Java 8. Execution requires that the application jar
is present and marked as executable. The command to execute the server is:
`java -jar simpleanalytics-1.0.0.jar`
### Data loading
The server serves data loaded from a CSV file. The file path (including the `file:` scheme prefix) can be passed using the parameter `input.filepath`
E.g. `java -jar simpleanalytics-1.0.0.jar --input.filepath=file:/full/path/to/input/file.csv`

In case no input filepath is defined the server loads data from a default CSV file, included in the executable
jar.
### Input file format
The input file loaded in the system needs to be a CSV with 5 values in each line.

E.g.
```
Datasource,Campaign,Daily,Clicks,Impressions
Datasource 1,Campaign 1,11/15/19,5,40
Datasource 1,Campaign 1,11/16/19,15,240
Datasource 1,Campaign 2,11/16/19,11,340
```
A header with 5 values is needed to be included at the start of the file, although the specific values for each header 
entry are not taken into account during loading it. 

### Execution
The server can be executed using the `java -jar` command. 

## Solution design and implementation
### Dependency injection
Dependencies are injected using constructor injection configured in `ApplicationConfiguration` using standard Spring annotations.
### Data loading
The server loads the data from the provided file location (using `--input.filepath`) or the default file included
in the jar by using `StartupRunner`, an implementation of `CommandLineRunner` which delegates the process of 
extracting, transforming, and loading the data to `CsvFileExtractor`, `CSVTransformer`, and `AnalyticsEntryLoader`.
#### CsvFileExtractor
`CsvFileExtractor` implements the `Extractor` and extracts valid CSV entries from the input file, while invalid
entries are discarded, creating a warning message in the server log.
#### CSVTransformer
`CSVTransformer` transforms the list of entries returned by `CsvFileExtractor` to a list of `AnalyticsEntry` instances
which is an immutable DTO.
#### AnalyticsEntryLoader
`AnalyticsEntryLoader` loads the list of `AnalyticsEntry` instances returned by `CSVTransformer` to the DB.
### Request handling
Incoming requests are handled by `AnalyticsController` which uses standard spring annotations such as `RequestBody`, `RequestMapping`, etc to
serialize the incoming JSON query to a `Query` instance that is then validated and executed. 
### Query validation
`QueryValidator` is used to validate the serialized `Query` and returns a `ValidationResult` indicating whether the Query is
valid or not.
### Query execution
Query execution is performed by `Query#execute` that constructs an internal JPA query by using enum constants and values
bound during the construction of the `Query` instance.

