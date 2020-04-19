package is.hi.hopon.ui.authentication.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.Response;

import is.hi.hopon.HoponContext;
import is.hi.hopon.R;
import is.hi.hopon.backend.HoponBackend;
import is.hi.hopon.backend.Models.Login.LoginRequest;
import is.hi.hopon.backend.Models.Login.LoginResponse;
import is.hi.hopon.backend.Models.Login.UserDetails;

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
            finish();
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
