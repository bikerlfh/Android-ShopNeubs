package co.com.neubs.shopneubs.classes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CLIENT_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.net.HttpURLConnection.HTTP_UNAVAILABLE;

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

    // Guarda el response completo de la peticion
    private transient String response;

    private transient int responseCode;

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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
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

    /**
     * Evalua si el detail devuelve token invalido
     * @return true si el token es invalido
     */
    public boolean isTokenInvalid(){
        return (detail != null && detail.length() > 0 && detail.toUpperCase().contains("TOKEN"));
    }

    public String getValidationNonFieldError(){
        return getMessages(nonFieldError);
    }
    public String getValidationUsername() {
        return getMessages(username);
    }
    public String getValidationEmail() {
        return getMessages(email);
    }
    public String getValidationPassword() {
        return getMessages(password);
    }
    public String getValidationError() {
        return getMessages(error);
    }

    private String getMessages(ArrayList<String> field){
        String message = null;
        for (String m: field) {
            message = (message == null)? m : message + ";" + m;
        }
        return message;
    }

    public boolean ok() { return  responseCode == HTTP_OK; }

    public boolean created() { return  responseCode == HTTP_CREATED; }

    public boolean badRequest() { return responseCode == HTTP_BAD_REQUEST; }

    public boolean unAuthorized() { return responseCode == HTTP_UNAUTHORIZED; }

    /**
     * Si hubo time out o el servidor no esta disponible
     * @return true si timeOut o Unavaible
     */
    public boolean timeOut() { return responseCode == HTTP_CLIENT_TIMEOUT || responseCode == HTTP_UNAVAILABLE; }

    public boolean serverError() { return responseCode == HTTP_INTERNAL_ERROR; }

    public boolean notFound() { return responseCode == HTTP_NOT_FOUND; }
}
