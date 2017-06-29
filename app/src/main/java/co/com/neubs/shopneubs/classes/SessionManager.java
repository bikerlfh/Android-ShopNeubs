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

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.models.APISincronizacion;
import co.com.neubs.shopneubs.classes.models.ItemCar;
import co.com.neubs.shopneubs.classes.models.SaldoInventario;
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

    private SessionManager(Context context) {
        // Se inicializa la instancia de dbManager
        DbManager.initInstance(context.getApplicationContext());
        token = null;
        Usuario usuario = new Usuario();
        cargarShopCar();
        if (usuario.getLoginUser())
            llenarCampos(usuario);
    }

    /**
     * Se obtiene la instancia
     * @param context puede ser null.
     * @return instance
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

    /***
     * obtiene la canitdad de items que hay en el carro
     */
    public int getCountItemsShopCar(){
        int countItems = 0;
        if (shopCar != null){
            for (ItemCar item:shopCar) {
                countItems += item.getCantidad();
            }
        }

        return countItems;
    }

    /**
     * Valida si el usuario está autenticado
     * en caso que el atributo no exista, se consulta en la db si existe un usuario con el token asignado
     * @return true si esta autenticado de lo contrario false
     */
    public boolean isAuthenticated(){
        return (token != null && !token.isEmpty());
    }

    /**
     * Crea la sesión del usuario, actualizando el campo Token el usuario
     * @param idUsuario el id del usuario
     * @param token el token
     * @return true s se crea la sesión de lo contrario false
     */
    public boolean createUserSession(int idUsuario, String token){
        // Se cierran las sessiones que tenga el usuario abierto
        closeUserSession();
        Usuario usuario = new Usuario();
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
     * @return true si se cierra la sesión de lo contrario false
     */
    public boolean closeUserSession(){
        try {
            Usuario usuario = new Usuario();
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

    /**
     * Visualiza un mensaje de que la sessión a expirado, cierra la sesión y finaliza la actividad
     * @param activity activity
     */
    public void closeSessionExpired(Activity activity){
        Toast.makeText(activity,activity.getString(R.string.msg_session_expired),Toast.LENGTH_SHORT).show();
        closeUserSession();
        activity.finish();
    }

    private void llenarCampos(Usuario usuario){
        this.idUsuario = usuario.getIdUsuario();
        this.username = usuario.getUsername();
        this.email = usuario.getEmail();
        this.token = usuario.getToken();
    }


    /**************************************************************************/
    //                        FUNCIONALIDAD DEL CARRO                          //
    /***************************************************************************/

    /**
     * Consulta el carrito
     */
    private void cargarShopCar(){
        final ItemCar itemCar = new ItemCar();
        this.shopCar = itemCar.getAllItemCar();
    }
    public void sincronizarPreciosShopCar(){
        APISincronizacion apiSincronizacion = new APISincronizacion();
        // Se consulta la ultima sincronzacion general
        if(apiSincronizacion.getLastGeneral()){
            if (validarSincronizacionShopCar(apiSincronizacion.getFecha())) {
                String data = "[";
                for (final ItemCar item : shopCar) {
                    data += "{\"idSaldoInventario\":" + item.getIdSaldoInventario() + "},";
                }
                data += "]";

                Map<String, String> params = new HashMap<>();
                params.put("data", data);

                APIRest.Async.post(APIRest.URL_PRODUCTO_SIMPLE, params, new IServerCallback() {
                    @Override
                    public void onSuccess(String json) {
                        SaldoInventario[] listadoSaldoInventario = APIRest.serializeObjectFromJson(json,SaldoInventario[].class);
                        if (listadoSaldoInventario != null){
                            for(SaldoInventario saldo:listadoSaldoInventario){
                                ItemCar item = getItemCarByIdSaldoInventario(saldo.getIdSaldoInventario());
                                if (item != null){
                                    if (saldo.getPrecioOferta() > 0)
                                        item.setPrecioVentaUnitario(saldo.getPrecioOferta());
                                    else
                                        item.setPrecioVentaUnitario(saldo.getPrecioVentaUnitario());
                                    item.update();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String message_error, APIValidations apiValidations) {
                        // Se borran  el carro.
                        deleteShopCar();
                    }
                });
            }

        }
    }
    /**
     * Valida si hay algun item en el carro con una fecha anterior a la ultima sincronización
     * de los precios
     * @param fechaApiSincronizacion ultima fecha de la api
     * @return true si se debe sincronizar
     */
    private boolean validarSincronizacionShopCar(String fechaApiSincronizacion){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date fecha = df.parse(df.format(new Date()));
            Date fechaItem = null;
            for (ItemCar item:shopCar) {
                fechaItem = df.parse(item.getFecha());
                if(fechaItem.before(fecha)){
                    fecha = fechaItem;
                }
            }
            Date fechaAPi = df.parse(fechaApiSincronizacion);
            if (fechaItem.before(fechaAPi))
                return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Agrega un ItemCar al carrito
     * @param itemCar itemCar a agregar
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
     * @param item a eliminar
     * @return true si se elimina el item del carro
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
     * @param id del carro a obtener
     * @return itemCar o null
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
     * @param idSaldoInventario del itemCar a obtener
     * @return itemCar o null
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
     * @return valorTotal
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
