package is.hi.hopon.ui.authentication.login;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import is.hi.hopon.HoponContext;
import is.hi.hopon.R;
import is.hi.hopon.backend.HoponBackend;
import is.hi.hopon.backend.Models.Core.Model;
import is.hi.hopon.backend.Models.Login.LoginRequest;
import is.hi.hopon.backend.Models.Login.LoginResponse;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedBundleInstance) {
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("login");
        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = ((EditText) findViewById(R.id.username)).getText().toString();
                String pwd = ((EditText) findViewById(R.id.password)).getText().toString();
                HoponBackend backend = HoponContext.getInstance(null).getBackend();
                backend.login(new LoginRequest(uname, pwd), new HoponBackend.HoponBackendResponse<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse response) {
                        finish();
                    }
                    @Override
                    public void onError(Exception error) {
                        Log.w("NOPE", error.toString());
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
