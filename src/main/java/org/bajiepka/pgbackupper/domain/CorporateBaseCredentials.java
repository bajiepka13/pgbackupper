package org.bajiepka.pgbackupper.domain;

/**
 * @author Valerii C.
 */

public class CorporateBaseCredentials {

    private String username;
    private String password;
    private boolean disconnectUsers;

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isDisconnectUsers() {
        return disconnectUsers;
    }

    public void setDisconnectUsers(boolean disconnectUsers) {
        this.disconnectUsers = disconnectUsers;
    }
}
