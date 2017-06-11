package co.com.neubs.shopneubs.classes;

import android.content.Context;

import co.com.neubs.shopneubs.classes.models.Usuario;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

/**
 * Created by bikerlfh on 6/11/17.
 */

public class UserSession {
    private int idUsuario;
    private String token;

    private static UserSession instance = null;

    public UserSession() {
        token = null;
    }

    public static UserSession getInstance(){
        if (instance==null)
            instance = new UserSession();
        return instance;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAuthenticated(){
        if (token != null || !token.isEmpty()){
            return true;
        }
        return false;
    }

    public boolean saveUserSession(Context context, String email,String username, String token){
        Usuario usuario = new Usuario(context);
        // Si ya hay un usuario logeado, se debe desloguear
        if (usuario.getLoginUser()){
            usuario.setToken(null);
            usuario.save();
        }
        // Se valida que el email o el username exista
        if (!email.isEmpty() || !username.isEmpty()) {
            if (!usuario.getByEmail(email)) {
                usuario.getByUserName(username);
            }
            // Si el usuario no esta guardado, se asigna el email y el username
            if (usuario.getIdUsuario() == 0) {

                // Se consulta el usuario y se guarda
                APIRest.Async.get("", new IServerCallback() {
                    @Override
                    public void onSuccess(String json) {

                    }

                    @Override
                    public void onError(String message_error, String response) {

                    }
                });

                usuario.setEmail(email);
                usuario.setUsername(username);
            }
            // Se guarda el token y el usuario
            usuario.setToken(token);
            usuario.save();
            return true;
        }
        return false;
    }

    public Usuario getUsuario(Context context){
        if (idUsuario>0){
            Usuario usuario = new Usuario(context);
            usuario.getById(idUsuario);
            return usuario;
        }
        return null;
    }

}
