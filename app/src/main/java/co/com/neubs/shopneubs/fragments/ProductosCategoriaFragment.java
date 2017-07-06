package co.com.neubs.shopneubs.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashMap;
import java.util.Map;

import co.com.neubs.shopneubs.PrincipalActivity;
import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.ConsultaPaginada;
import co.com.neubs.shopneubs.controls.NavigationViewFiltro;
import co.com.neubs.shopneubs.controls.VistaFiltroPrincipal;
import co.com.neubs.shopneubs.interfaces.IServerCallback;


public class ProductosCategoriaFragment extends Fragment implements PrincipalActivity.OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String PARAM_CODIGO_CATEGORIA = "CODIGO_CATEGORIA";
    public static final String PARAM_CODIGO_MARCA = "CODIGO_MARCA";
    public static final String PARAM_URL_REQUEST = "URL_REQUEST";

    // TODO: Rename and change types of parameters
    private String codigoCategoria;
    private String codigoMarca;
    /**
     * Almacena la url al cual se desea se realize la petición a la API
     */
    private String urlRequest;
    private Map<String,String> parametrosRequest;

    /**
     * vistaFiltroPrincipal
     */
    private VistaFiltroPrincipal vistaFiltroPrincipal;

    public ProductosCategoriaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductosCategoriaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductosCategoriaFragment newInstance(String param1, String param2) {
        ProductosCategoriaFragment fragment = new ProductosCategoriaFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CODIGO_CATEGORIA, param1);
        args.putString(PARAM_CODIGO_MARCA, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            codigoCategoria = getArguments().getString(PARAM_CODIGO_CATEGORIA,null);
            codigoMarca = getArguments().getString(PARAM_CODIGO_MARCA,null);
            urlRequest = getArguments().getString(PARAM_URL_REQUEST,"");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_productos_categoria, container, false);
        // Inflate the layout for this fragment

        vistaFiltroPrincipal = (VistaFiltroPrincipal) view.findViewById(R.id.vista_filtro_principal);
        parametrosRequest = new HashMap<>();
        if (codigoCategoria != null) {
            parametrosRequest.put("categoria",codigoCategoria);
        }
        if (codigoMarca != null){
            parametrosRequest.put("marca",codigoMarca);
        }


        // Se asigna el evento click del boton aplicarFiltro
        vistaFiltroPrincipal.setOnClickListenerAplicarFiltro(new NavigationViewFiltro.OnClickListenerAplicarFiltro() {
            @Override
            public void onClick(View v, Map<String, String> filtro) {
                if (filtro.size() > 0) {
                    // se agregan al filtro los parametros iniciales
                    filtro.putAll(parametrosRequest);
                    consultarPeticionAPI(urlRequest, filtro);
                    vistaFiltroPrincipal.setFiltroAplicado(true);
                    ((PrincipalActivity) getActivity()).setOnBackPressedListener(ProductosCategoriaFragment.this);
                }
                vistaFiltroPrincipal.closeDrawer();
            }
        });
        // Se asigna el evento click del boton limpiarFiltro
        vistaFiltroPrincipal.setOnClickListenerLimpiarFiltro(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarPeticionAPI(urlRequest,parametrosRequest);
            }
        });

        consultarPeticionAPI(urlRequest,parametrosRequest);

        return view;
    }

    private void consultarPeticionAPI(String url, Map<String,String> params){
        vistaFiltroPrincipal.showLoadingProgressBar(true);
        APIRest.Async.get(url,params, new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                final ConsultaPaginada consultaPaginada = APIRest.serializeObjectFromJson(json,ConsultaPaginada.class);
                // Se visualizan los productos en el recycleView de la vistaFiltroPrincipal
                vistaFiltroPrincipal.showProductos(consultaPaginada);
            }

            @Override
            public void onError(String message_error, APIValidations apiValidations) {
                vistaFiltroPrincipal.showLoadingProgressBar(false);
                //Snackbar.make(view,"Error:"+message_error,Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }


    /**
     * Cuando se hace el onBackPressed, se verifica si hay un filtro aplicado
     * si lo hay, se realiza la peticion principal a la API, de lo contrario se llama el onBackPressed de la actividad
     */
    @Override
    public void doBack() {
        if (vistaFiltroPrincipal.isFiltroAplicado()) {
            consultarPeticionAPI(urlRequest, parametrosRequest);
            vistaFiltroPrincipal.setFiltroAplicado(false);
        }
        else{
            ((PrincipalActivity)getActivity()).setOnBackPressedListener(null);
            getActivity().onBackPressed();
        }
    }
}
