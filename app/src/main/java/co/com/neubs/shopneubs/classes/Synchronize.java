package co.com.neubs.shopneubs.classes;

import android.content.Context;
import android.util.Log;
import android.view.View;
import co.com.neubs.shopneubs.classes.models.Categoria;
import co.com.neubs.shopneubs.classes.models.Marca;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

/**
 * Created by bikerlfh on 6/2/17.
 */

public class Synchronize {

    // Realiza la sincronización de las marcas
    public static boolean SyncronizeMarcas(Context context){
        try {
            String url = APIRest.URL_API + "marca/";
            String json = HttpRequest.get(url).accept("application/json").body();

            Marca[] marcas = APIRest.serializeObjectFromJson(json, Marca[].class);
            if (marcas != null && marcas.length > 0) {
                Marca marca = new Marca(context);
                for (Marca m : marcas) {
                    if (!marca.getMarcaByid(m.getIdMarca())) {
                        m.initDbManager(context);
                        m.save();
                    }
                }
            }
            return true;
        }
        catch (Exception ex){
            Log.d("SincronizacionMarcas",ex.getMessage());
        }
        return false;
        /*APIRest.get("", new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                Marca[] marcas = APIRest.serializeObjectFromJson(json, Marca[].class);
                if (marcas != null && marcas.length > 0)
                {
                    Marca marca = new Marca(view.getContext());
                    for (Marca m:marcas) {
                        if (!marca.getMarcaByid(m.getIdMarca())) {
                            m.initDbManager(view.getContext());
                            m.save();
                        }
                    }
                }
            }

            @Override
            public void onError(String message_error) {

            }
        });*/
    }

    public static  void SyncronizeCategorias(final View view){
        APIRest.get("", new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                Categoria[] categorias = APIRest.serializeObjectFromJson(json,Categoria[].class);
                if (categorias != null && categorias.length > 0){
                    Categoria categoria= new Categoria(view.getContext());
                    for(Categoria c : categorias){
                        if (!categoria.getCategoriaByid(c.getIdCategoria())){
                            c.initDbManager(view.getContext());
                            c.save();
                        }
                    }
                }
            }

            @Override
            public void onError(String message_error) {

            }
        });
    }

}
