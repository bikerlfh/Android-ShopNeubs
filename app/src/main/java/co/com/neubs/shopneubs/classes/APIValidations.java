package co.com.neubs.shopneubs.classes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by bikerlfh on 6/12/17.
 */

public class APIValidations {

    @SerializedName("non_field_errors")
    private ArrayList<String> nonFieldError;

    private ArrayList<String> username;
    private ArrayList<String> email;
    private ArrayList<String> password;
    private ArrayList<String> error;
    private String detail;
    // cuando el usuario no esta creado como cliente
    private String usuarioNoCliente;

    public ArrayList<String> getNonFieldError() {
        return nonFieldError;
    }

    public void setNonFieldError(ArrayList<String> nonFieldError) {
        this.nonFieldError = nonFieldError;
    }

    public ArrayList<String> getUsername() {
        return username;
    }

    public void setUsername(ArrayList<String> username) {
        this.username = username;
    }

    public ArrayList<String> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
    }

    public ArrayList<String> getPassword() {
        return password;
    }

    public void setPassword(ArrayList<String> password) {
        this.password = password;
    }

    public ArrayList<String> getError() {
        return error;
    }

    public void setError(ArrayList<String> error) {
        this.error = error;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUsuarioNoCliente() {
        return usuarioNoCliente;
    }

    public void setUsuarioNoCliente(String usuarioNoCliente) {
        this.usuarioNoCliente = usuarioNoCliente;
    }

    public boolean isNoFieldError(){
        return nonFieldError != null && nonFieldError.size() > 0;
    }

    public boolean isUsername(){
        return username != null && username.size() > 0;
    }

    public boolean isEmail(){
        return email != null && email.size() > 0;
    }

    public boolean isPassword(){
        return password != null && password.size() > 0;
    }

    public boolean isError(){
        return error != null && error.size() > 0;
    }

    public boolean isUsuarioNoCliente(){ return usuarioNoCliente!=null && usuarioNoCliente.length() > 0;}

    public String getValidationNonFieldError(){
        return getMessges(nonFieldError);
    }
    public String getValidationUsername() {
        return getMessges(username);
    }
    public String getValidationEmail() {
        return getMessges(email);
    }
    public String getValidationPassword() {
        return getMessges(password);
    }
    public String getValidationError() {
        return getMessges(error);
    }

    private String getMessges(ArrayList<String> field){
        String message = null;
        for (String m: field) {
            message = (message == null)? m : message + ";" + m;
        }
        return message;
    }
}
