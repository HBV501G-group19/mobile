package is.hi.hopon.backend.Models.Login;

import androidx.transition.Visibility;

import is.hi.hopon.backend.Models.Core.Model;

public class UserDetails extends Model {
    @Model.Field
    private Integer id;

    public Integer getId() { return id; }

    @Model.Field
    private String username;

    public String getUsername() { return username; }
}
