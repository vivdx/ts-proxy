#install.packages("rgdal")
library(rgdal)
sp.theme(TRUE)
library(SPARQL)   # SPARQL querying package

setClass("GeostatisticalDataFrame", contains = "SpatialPointsDataFrame",
                   representation(window = "SpatialPolygons"))

is.sum = function(x) {
  paste(deparse(x), collapse="") == paste(deparse(sum),collapse="")
}

aggregate.GeostatisticalDataFrame = function(x, by, FUN = mean, ...) {
  if (is.sum(FUN))
    warning("aggregation using a sum function is not considered meaningful for Geostatistical data")
  aggregate(as(x, "SpatialPointsDataFrame"), by, FUN, ...)
}

# augmented generic aggregate function for PointPattern; a warning is thrown, if the function is not a sum function
#
aggregate.PointPatternDataFrame = function(x, by, FUN = mean, ...) {
  if (!is.sum(FUN))
    warning("aggregation using a non-sum function is not considered meaningful for Point Pattern data")
  aggregate(as(x, "SpatialPointsDataFrame"), by, FUN, ...)
}

############# returns data with variable type

endpoint <- "http://localhost:8081/parliament/sparql"
getVariableTypeInfo <- function(endpoint){
  query <- "PREFIX mss: <http://www.meaningfulspatialstatistics.org/theories/MeaningfulSpatialStatistics.owl#>
  SELECT *
WHERE {
    ?url mss:ofVariableType ?VariableType;

}"
  res <- SPARQL(url=endpoint, query)$results
  return(res)
}
DataSets <- getVariableTypeInfo(endpoint)
DataSets

#query to find geostatistical datasets

endpoint <- "http://localhost:8081/parliament/sparql"
getGeostatInfo <- function(endpoint){
  query <- "PREFIX mss: <http://www.meaningfulspatialstatistics.org/theories/MeaningfulSpatialStatistics.owl#>
  SELECT DISTINCT
  ?url ?phenomenon
  WHERE {
  ?url mss:generatedBy ?obsFun;
  mss:represents ?phenomenon.
  ?obsFun  mss:ofVariableType <http://www.meaningfulspatialstatistics.org/theories/MeaningfulSpatialStatistics.owl#GeostatisticalVariable>.
  }"
  res <- SPARQL(url=endpoint, query)$results
  return(res)
}
geostatisticalDataSets <- getGeostatInfo(endpoint)
geostatisticalDataSets

#############

#import data
surfaceTemp = readGDAL("MOD11C1_M_LSTDA_2014-07-01_rgb_360x180.FLOAT.TIFF")
surfaceTemp$band1 = replace(surfaceTemp$band1, which(surfaceTemp$band1== 99999), NA)
str(surfaceTemp)
spplot(surfaceTemp)
# convert from SpatialGridDataFrame to GeostatisticalDataFrame
surfaceTemp = as(surfaceTemp, "SpatialPointsDataFrame")
surfaceTemp = as(surfaceTemp, "GeostatisticalDataFrame")

#define grid for aggregation
bbox(surfaceTemp)
dx <- seq(-175,175,by=5)
dy <- seq(-85,85,by=5)
catch.grid <- expand.grid(dx,dy)
#setting the names of the columns of the grid
names(catch.grid) <- c("x","y")
#setting the coordinates of the grid
coordinates(catch.grid) <- ~x+y
gridded(catch.grid) = TRUE
proj4string(catch.grid) <- proj4string(surfaceTemp)
# non-meaningful aggregation
test = aggregate(surfaceTemp, by=catch.grid, FUN=sum)
spplot(test)
# meaningful aggregation
test = aggregate(surfaceTemp, by=catch.grid, FUN=mean)
spplot(test)
