package is.hi.hopon.backend.Models.Login;

import is.hi.hopon.backend.Models.Core.Model;

public class LoginResponse extends Model {
    public LoginResponse(){}

    @Model.Field
    public String username;

    @Model.Field
    public String token;

    @Model.Field
    public String id;
}
