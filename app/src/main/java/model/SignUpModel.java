package model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SignUpModel implements Serializable {

    public String user_email = "";
    public String user_password = "";
    public String user_name = "";
    public String user_sex = "";
    public String user_day = "";

    public SignUpModel() {
    }

    public SignUpModel(String user_email, String user_password, String user_name, String user_sex, String user_day) {
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_name = user_name;
        this.user_sex = user_sex;
        this.user_day = user_day;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_email", user_email);
        result.put("user_password", user_password);
        result.put("user_name", user_name);
        result.put("user_sex", user_sex);
        result.put("user_day", user_day);
        return result;
    }
}
