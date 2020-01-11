package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.geometry.SubpolylineHelper;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.transport.TransportFactory;
import com.yandex.mapkit.transport.masstransit.MasstransitOptions;
import com.yandex.mapkit.transport.masstransit.MasstransitRouter;
import com.yandex.mapkit.transport.masstransit.Route;
import com.yandex.mapkit.transport.masstransit.Section;
import com.yandex.mapkit.transport.masstransit.SectionMetadata;
import com.yandex.mapkit.transport.masstransit.Session;
import com.yandex.mapkit.transport.masstransit.TimeOptions;
import com.yandex.mapkit.transport.masstransit.Transport;

import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;



/**
 * This example shows how to build public transport routes between two points,
 * and how to handle route sections and vehicle types lists
 * Note: Masstransit routing API calls count towards MapKit daily usage limits. Learn more at
 * https://tech.yandex.ru/mapkit/doc/3.x/concepts/conditions-docpage/#conditions__limits
 */
public class MainActivity extends AppCompatActivity
        implements Session.RouteListener {
    /**
     * Replace "your_api_key" with a valid developer key.
     * You can get it at the https://developer.tech.yandex.ru/ website.
     */


    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;

    private final String MAPKIT_API_KEY = "9459daac-c0d3-4322-960e-87b020a031ac";





//    private final Point ROUTE_START_LOCATION = new Point(55.760246, 37.625272);
//    private final Point ROUTE_END_LOCATION = new Point(55.767923, 37.629108);

//    private final Point TARGET_LOCATION = new Point(55.752078, 37.592664);
//    private final Point WAY_POINT = new Point(55.760965, 37.624515);
//    private final Point ROUTE_START_LOCATION = new Point(55.760246, 37.625272);
//    private final Point WAY_POINT3 = new Point(55.758567, 37.624306);
//    private final Point ROUTE_END_LOCATION = new Point(55.755672, 37.619805);


    private MapView mapView;
    private MapObjectCollection mapObjects;
    private MasstransitRouter mtRouter;
    public void ConnectToDatabase(){
        try {

            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String username = "ITS1920-vlasov";
            String password = "FxLHLHQ6OW";
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://vds-2019.1586.su/DATABASE;user=" + username + ";password=" + password);

            Log.w("Connection","open");
            Statement stmt = DbConn.createStatement();
            ResultSet reset = stmt.executeQuery(" select * from Points ");

            DbConn.close();
            Log.w("Error connection","");
        } catch (Exception e)
        {
            Log.w("Error connection","" + e.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ConnectToDatabase();
        mDBHelper = new DBHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        String product = "";
        String product1 = "";
        String product2 = "";
        Cursor cursor = mDb.rawQuery("SELECT * FROM Coord", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product += cursor.getString(0) + ",";
            product1 += cursor.getString(1) + ",";
            product2 += cursor.getString(2) + ",";
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("mLog",product+'\n'+'\n'+'='+product1+"\n"+product2);

        ArrayList<String> x = new ArrayList<>(Arrays.asList(product.split(",")));
        ArrayList<String> y = new ArrayList<>(Arrays.asList(product1.split(",")));
        ArrayList<String> tag = new ArrayList<>(Arrays.asList(product2.split(",")));


         final Point TARGET_LOCATION = new Point(55.752078, 37.592664);


         float x1 = Float.parseFloat(x.get(0));
         float y1 = Float.parseFloat(y.get(0));
         float x2 = Float.parseFloat(x.get(1));
         float y2 = Float.parseFloat(y.get(1));
         float x3 = Float.parseFloat(x.get(2));
         float y3 = Float.parseFloat(y.get(2));
         float x4 = Float.parseFloat(x.get(3));
         float y4 = Float.parseFloat(y.get(3));
         String tag1 = tag.get(0);
         String tag2 = tag.get(1);
         String tag3 = tag.get(2);
         String tag4 = tag.get(3);


         final Point ROUTE_START_LOCATION = new Point(x1, y1);
         final Point WAY_POINT3 = new Point(x2, y2);
         final Point WAY_POINT1 = new Point(x3, y3);
         final Point ROUTE_END_LOCATION = new Point(x4, y4);





        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        TransportFactory.initialize(this);

        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        mapView = (MapView)findViewById(R.id.mapview);

        // And to show what can be done with it, we move the camera to the center of Saint Petersburg.
        mapView.getMap().move(
                new CameraPosition(TARGET_LOCATION, 12.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);

        mapObjects = mapView.getMap().getMapObjects().addCollection();


        MasstransitOptions options = new MasstransitOptions(
                new ArrayList<String>(),
                new ArrayList<String>(),
                new TimeOptions());
        List<RequestPoint> points = new ArrayList<RequestPoint>();
        points.add(new RequestPoint(ROUTE_START_LOCATION, RequestPointType.WAYPOINT, null));
        points.add(new RequestPoint(WAY_POINT3, RequestPointType.WAYPOINT, null));
        points.add(new RequestPoint(ROUTE_END_LOCATION, RequestPointType.WAYPOINT, null));
//        points.add(new RequestPoint(WAY_POINT, RequestPointType.WAYPOINT, null));
//        points.add(new RequestPoint(WAY_POINT2, RequestPointType.WAYPOINT, null));
//        points.add(new RequestPoint(WAY_POINT3, RequestPointType.WAYPOINT, null));
//        points.add(new RequestPoint(WAY_POINT4, RequestPointType.WAYPOINT, null));
        
        mtRouter = TransportFactory.getInstance().createMasstransitRouter();
        mtRouter.requestRoutes(points, options, this);

    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onMasstransitRoutes(List<Route> routes) {
        // In this example we consider first alternative only
        if (routes.size() > 0) {
            for (Section section : routes.get(0).getSections()) {
                drawSection(
                        section.getMetadata().getData(),
                        SubpolylineHelper.subpolyline(
                                routes.get(0).getGeometry(), section.getGeometry()));
            }
        }
    }

    @Override
    public void onMasstransitRoutesError(com.yandex.runtime.Error error) {
        String errorMessage = getString(R.string.unknown_error_message);
        if (error instanceof RemoteError) {
            errorMessage = getString(R.string.remote_error_message);
        } else if (error instanceof NetworkError) {
            errorMessage = getString(R.string.network_error_message);
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void drawSection(SectionMetadata.SectionData data,
                             Polyline geometry) {
        // Draw a section polyline on a map
        // Set its color depending on the information which the section contains
        PolylineMapObject polylineMapObject = mapObjects.addPolyline(geometry);
        // Masstransit route section defines exactly one on the following
        // 1. Wait until public transport unit arrives
        // 2. Walk
        // 3. Transfer to a nearby stop (typically transfer to a connected
        //    underground station)
        // 4. Ride on a public transport
        // Check the corresponding object for null to get to know which
        // kind of section it is
        if (data.getTransports() != null) {
            // A ride on a public transport section contains information about
            // all known public transport lines which can be used to travel from
            // the start of the section to the end of the section without transfers
            // along a similar geometry
            for (Transport transport : data.getTransports()) {
                // Some public transport lines may have a color associated with them
                // Typically this is the case of underground lines
                if (transport.getLine().getStyle() != null) {
                    polylineMapObject.setStrokeColor(
                            // The color is in RRGGBB 24-bit format
                            // Convert it to AARRGGBB 32-bit format, set alpha to 255 (opaque)
                            transport.getLine().getStyle().getColor() | 0xFF000000
                    );
                    return;
                }
            }
            // Let us draw bus lines in green and tramway lines in red
            // Draw any other public transport lines in blue
            HashSet<String> knownVehicleTypes = new HashSet<>();
            knownVehicleTypes.add("bus");
            knownVehicleTypes.add("tramway");
            for (Transport transport : data.getTransports()) {
                String sectionVehicleType = getVehicleType(transport, knownVehicleTypes);
                if (sectionVehicleType.equals("bus")) {
                    polylineMapObject.setStrokeColor(0xFF00FF00);  // Green
                    return;
                } else if (sectionVehicleType.equals("tramway")) {
                    polylineMapObject.setStrokeColor(0xFFFF0000);  // Red
                    return;
                }
            }
            polylineMapObject.setStrokeColor(0xFF0000FF);  // Blue
        } else {
            // This is not a public transport ride section
            // In this example let us draw it in black
            polylineMapObject.setStrokeColor(0xFF000000);  // Black
        }
    }

    private String getVehicleType(Transport transport, HashSet<String> knownVehicleTypes) {
        // A public transport line may have a few 'vehicle types' associated with it
        // These vehicle types are sorted from more specific (say, 'histroic_tram')
        // to more common (say, 'tramway').
        // Your application does not know the list of all vehicle types that occur in the data
        // (because this list is expanding over time), therefore to get the vehicle type of
        // a public line you should iterate from the more specific ones to more common ones
        // until you get a vehicle type which you can process
        // Some examples of vehicle types:
        // "bus", "minibus", "trolleybus", "tramway", "underground", "railway"
        for (String type : transport.getLine().getVehicleTypes()) {
            if (knownVehicleTypes.contains(type)) {
                return type;
            }
        }
        return null;
    }
}