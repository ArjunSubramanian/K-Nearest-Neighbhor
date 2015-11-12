
public class Result {
String Label;
double distance;
public Result(double distance,String label){
	this.Label = label;
	this.distance=distance;
}
public String getLabel() {
	return Label;
}
public void setLabel(String label) {
	Label = label;
}
public double getDistance() {
	return distance;
}
public void setDistance(double distance) {
	this.distance = distance;
}

}
