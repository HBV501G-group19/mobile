package is.hi.hopon.ui.authentication.login;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import is.hi.hopon.HoponContext;
import is.hi.hopon.R;
import is.hi.hopon.backend.HoponBackend;
import is.hi.hopon.backend.Models.Core.Model;
import is.hi.hopon.backend.Models.Login.LoginRequest;
import is.hi.hopon.backend.Models.Login.LoginResponse;
import is.hi.hopon.backend.Models.Login.UserDetails;
import is.hi.hopon.backend.Models.Ride.Geo;
import is.hi.hopon.backend.Models.Ride.Ride;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedBundleInstance) {
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Button login = findViewById(R.id.login);

        HoponBackend backend = HoponContext.getInstance().getBackend();
        if(HoponContext.getInstance().getUser() != null)
        {

            Log.i("HLOG/LoginActivity", "User logged, skipping login screen");
/*
            Ride ride = new Ride();
            ride.setId(1);
            ride.setDriver(2);
            Geo origin = new Geo();
            origin.setGeoType("Point");
            origin.setCoordinates(Arrays.asList(3.14, 4.15));
            ride.setOrigin(origin);
            ride.setCreated(new Date());

            UserDetails d = new UserDetails();
            d.setId(1);
            d.setUsername("Lollari");
            d.setRides(Arrays.asList(ride));

            try {
                JSONObject rs = d.toJson();
                Log.w("HLOG/inout-in", rs.toString());

                UserDetails nr = new UserDetails().fromJson(rs);
                nr.setId(17);

                Log.w("HLOG/inout-o", nr.getRides().toString());
                Log.w("HLOG/inout-out", nr.toJson().toString());
            }catch (Model.SerializationError ser)
            {
                Log.w("HLOG/ser", "Cannot serialize " + ser.toString());
            }
*/
            backend.userDetails(2, new HoponBackend.HoponBackendResponse<UserDetails>() {
                @Override
                public void onSuccess(UserDetails response) {
                    Log.i("HLOG/loll", response.getRides().toString());
                }

                @Override
                public void onError(Exception error) {
                    Log.w("HLOG/loll", error.toString());
                }
            });


/*********          ************
            finish();
************         *********/
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setEnabled(false);
                String uname = ((EditText) findViewById(R.id.username)).getText().toString();
                String pwd = ((EditText) findViewById(R.id.password)).getText().toString();
                backend.login(new LoginRequest(uname, pwd), new HoponBackend.HoponBackendResponse<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse response) {
                        HoponContext.getInstance().logIn(response);
                        finish();
                    }
                    @Override
                    public void onError(Exception error) {
                        login.setEnabled(true);
                        if(login.getText().toString().equals("Try again")) {
                            login.setText("Nope");
                        }else {
                            login.setText("Try again");
                        }
                    }
                });
            }
        });
    }
}
