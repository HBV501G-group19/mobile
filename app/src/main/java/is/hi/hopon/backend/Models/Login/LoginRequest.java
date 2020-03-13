package is.hi.hopon.backend.Models.Login;

import is.hi.hopon.backend.Models.Core.Model;


public class LoginRequest extends Model {
    @Model.Field(required = true)
    public String username;

    @Model.Field(required = true)
    public String password;

    public LoginRequest(){}
    public LoginRequest(String uname, String pwd)
    {
        username = uname;
        password = pwd;
    }
}
