package co.com.neubs.shopneubs.classes;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.models.APISincronizacion;
import co.com.neubs.shopneubs.classes.models.ItemCar;
import co.com.neubs.shopneubs.classes.models.Usuario;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

/**
 * Created by bikerlfh on 6/11/17.
 */

public class SessionManager {
    private final String TAG = "SESSION_MANAGER";

    private int idUsuario;
    private String username;
    private String email;
    private String token;

    // Guarda el carrito
    private ArrayList<ItemCar> shopCar;

    private static SessionManager instance = null;

    public SessionManager(Context context) {
        token = null;
        Usuario usuario = new Usuario(context);
        cargarShopCar(context);
        if (usuario.getLoginUser())
            llenarCampos(usuario);
    }

    /**
     * Se obtiene la instancia
     * @param context puede ser null.
     * @return
     */
    public static SessionManager getInstance(Context context){
        if (instance==null)
            instance = new SessionManager(context);
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

    public ArrayList<ItemCar> getShopCar() {
        if (shopCar == null)
            shopCar = new ArrayList<>();
        return shopCar;
    }

    public void setShopCar(ArrayList<ItemCar> shopCar) {
        this.shopCar = shopCar;
    }

    /**
     * Valida si el usuario está autenticado
     * en caso que el atributo no exista, se consulta en la db si existe un usuario con el token asignado
     * @return
     */
    public boolean isAuthenticated(){
        return (token != null && !token.isEmpty());
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
    public boolean closeUserSession(final Context context){
        try {
            Usuario usuario = new Usuario(context);
            if (usuario.getLoginUser()){
                APIRest.Async.post(APIRest.URL_LOGOUT, null, new IServerCallback() {
                    @Override
                    public void onSuccess(String json) {
                        //Toast.makeText(context,"Logout success",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String message_error, APIValidations apiValidations) {
                        /*if (apiValidations!=null && !apiValidations.isTokenInvalid())
                            Toast.makeText(context,"Logout unsuccess",Toast.LENGTH_SHORT).show();*/
                    }
                });
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

    public void closeSessionExpired(Activity activity){
        Toast.makeText(activity,activity.getString(R.string.msg_session_expired),Toast.LENGTH_SHORT).show();
        closeUserSession(activity.getApplicationContext());
        activity.finish();
    }

    private void llenarCampos(Usuario usuario){
        this.idUsuario = usuario.getIdUsuario();
        this.username = usuario.getUsername();
        this.email = usuario.getEmail();
        this.token = usuario.getToken();
    }


    /***************************************************************************/
    //                        FUNCIONALIDAD DEL CARRO                          //
    /***************************************************************************/

    /**
     * Consulta el carrito
     * @param context
     */
    private void cargarShopCar(Context context){
        final ItemCar itemCar = new ItemCar(context);
        this.shopCar = itemCar.getAllItemCar();

        if (this.shopCar != null && this.shopCar.size() > 0){
            APISincronizacion apiSincronizacion = new APISincronizacion(context);
            // Se consulta la ultima sincronzacion general
            if(apiSincronizacion.getLastGeneral()){
                for(final ItemCar item:shopCar){
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    try {
                        Date fechaItem = df.parse(item.getFecha());
                        Date fechaSincronizacion = df.parse(apiSincronizacion.getFecha());
                        // si la fecha del itemCar es menor al de la sincronización, se actualiza el precio
                        if (fechaItem.before(fechaSincronizacion)){
                            APIRest.Async.get("", new IServerCallback() {
                                @Override
                                public void onSuccess(String json) {

                                }

                                @Override
                                public void onError(String message_error, APIValidations apiValidations) {
                                    item.delete();
                                }
                            });
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Agrega un ItemCar al carrito
     * @param itemCar
     */
    public boolean addItemCar(ItemCar itemCar){
        if (this.shopCar == null)
            this.shopCar = new ArrayList<>();

        // Se valida que el saldoInventairo no esté agregado en el carro
        if (this.getItemCarByIdSaldoInventario(itemCar.getIdSaldoInventario()) == null) {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            itemCar.setFecha(df.format(new Date()));

            if (itemCar.save()) {
                this.shopCar.add(itemCar);
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina el item del carro
     * @param item
     * @return
     */
    public boolean deleteItemShopCar(ItemCar item){
        if (shopCar != null && shopCar.size() > 0) {
            if (shopCar.remove(item)) {
                if (!item.delete()) {
                    shopCar.add(item);
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina todos los items del Carrito
     * este método debe ser llamado cuando el pedido se ha guardado con exito
     */
    public void deleteShopCar(){
        if (shopCar != null && shopCar.size() > 0){
            for(ItemCar item:shopCar){
                item.delete();
            }
            shopCar = new ArrayList<>();
        }
    }

    /**
     * Obtiene un ItemCar por el id
     * @param id
     * @return
     */
    public ItemCar getItemCarById(int id){
        if (shopCar != null)
            for (ItemCar item: shopCar) {
                if (item.getIdItemCar() == id)
                    return item;
            }
        return null;
    }
    /**
     * Obtiene un ItemCar por el idSaldoInventario
     * @param idSaldoInventario
     * @return
     */
    public ItemCar getItemCarByIdSaldoInventario(int idSaldoInventario){
        if (shopCar != null)
            for (ItemCar item: shopCar) {
                if (item.getIdSaldoInventario() == idSaldoInventario)
                    return item;
            }
        return null;
    }

    /**
     * Obtiene el valor total del carro
     * @return
     */
    public float getValorTotalShopCar(){
        float valorTotal = 0;
        if (this.shopCar != null){
            for (ItemCar item: shopCar) {
                valorTotal += item.getValorTotal();
            }
        }
        return valorTotal;
    }
}
