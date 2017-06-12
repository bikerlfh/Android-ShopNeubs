package co.com.neubs.shopneubs.classes;
import android.content.Context;
import android.util.Log;
import co.com.neubs.shopneubs.classes.models.Usuario;

/**
 * Created by bikerlfh on 6/11/17.
 */

public class SessionManager {
    private final String TAG = "SESSION_MANAGER";

    private int idUsuario;
    private String username;
    private String email;
    private String token;

    private static SessionManager instance = null;

    public SessionManager() {
        token = null;
    }

    public static SessionManager getInstance(){
        if (instance==null)
            instance = new SessionManager();
        return instance;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    /**
     * Valida si el usuario está autenticado
     * en caso que el atributo no exista, se consulta en la db si existe un usuario con el token asignado
     * @param context
     * @return
     */
    public boolean isAuthenticated(Context context){
        if (token != null && !token.isEmpty()){
            return true;
        }
        else{
            Usuario usuario = new Usuario(context);
            if (usuario.getLoginUser()){
                llenarCampos(usuario);
                return true;
            }
        }
        return false;
    }

    /**
     * Crea la sesión del usuario, actualizando el campo Token el usuario
     * @param context
     * @param idUsuario
     * @param token
     * @return
     */
    public boolean createUserSession(Context context, int idUsuario, String token){
        // Se cierran las sessiones que tenga el usuario abierto
        closeUserSession(context);
        Usuario usuario = new Usuario(context);
        if (usuario.getById(idUsuario)) {
            usuario.setToken(token);
            usuario.update();

            llenarCampos(usuario);
            return true;
        }
        return false;
    }

    /**
     * Cierra la sesión del usuario activo
     * @param context
     * @return
     */
    public boolean closeUserSession(Context context){
        try {
            Usuario usuario = new Usuario(context);
            if (usuario.getLoginUser()){
                usuario.setToken(null);
                usuario.update();
            }
            this.idUsuario = 0;
            this.email = null;
            this.token = null;
            return true;
        }
        catch(Exception e){
            Log.d(TAG,e.getMessage());
        }
        return false;
    }

    private void llenarCampos(Usuario usuario){
        this.idUsuario = usuario.getIdUsuario();
        this.username = usuario.getUsername();
        this.email = usuario.getEmail();
        this.token = usuario.getToken();
    }
}
