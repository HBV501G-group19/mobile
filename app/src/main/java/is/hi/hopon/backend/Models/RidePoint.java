package is.hi.hopon.backend.Models;

import org.json.JSONArray;
import org.json.JSONException;

public class RidePoint
{
    public RidePoint(double x, double y)
    {
        this.setX(x);
        this.setY(y);
    }

    public RidePoint(JSONArray jsonPoint) throws JSONException {
        this.setX(jsonPoint.getDouble(0));
        this.setY(jsonPoint.getDouble(1));
    }

    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
