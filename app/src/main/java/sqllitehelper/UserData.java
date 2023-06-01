package sqllitehelper;

public class UserData {

    int id;
    String fullName,username,password, phoneNumber;

    public UserData(String fullName, String username, String password, String phoneNumber) {

        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
