package co.com.neubs.shopneubs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.BoringLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.Synchronize;
import co.com.neubs.shopneubs.classes.models.APISincronizacion;
import co.com.neubs.shopneubs.classes.models.Departamento;
import co.com.neubs.shopneubs.classes.models.Municipio;
import co.com.neubs.shopneubs.classes.models.Pais;
import co.com.neubs.shopneubs.classes.models.Perfil;
import co.com.neubs.shopneubs.classes.models.TipoDocumento;
import co.com.neubs.shopneubs.interfaces.IServerCallback;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditProfileActivity extends AppCompatActivity {

    private final SessionManager sessionManager = SessionManager.getInstance();

    private MaterialSpinner spnTipoDocumento,spnPais,spnDepartamento,spnMunicipio;
    private EditText txtNit,txtPrimerNombre,txtSegundoNombre,txtPrimerApellido,txtSegundoApellido,txtDireccion,txtTelefono;

    private TipoDocumento tipoDocumento;
    private Pais pais;
    private Departamento departamento;
    private Municipio municipio;

    List<Pais> listadoPais;
    List<Departamento> listadoDepartamento;
    List<Municipio> listadoMunicipio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spnTipoDocumento = (MaterialSpinner) findViewById(R.id.spn_tipo_documento);
        spnPais = (MaterialSpinner) findViewById(R.id.spn_pais);
        spnDepartamento = (MaterialSpinner) findViewById(R.id.spn_departamento);
        spnMunicipio = (MaterialSpinner) findViewById(R.id.spn_municipio);

        txtNit = (EditText) findViewById(R.id.txt_nit);
        txtPrimerNombre = (EditText) findViewById(R.id.txt_primer_nombre);
        txtSegundoNombre = (EditText) findViewById(R.id.txt_segundo_nombre);
        txtPrimerApellido = (EditText) findViewById(R.id.txt_primer_apellido);
        txtSegundoApellido = (EditText) findViewById(R.id.txt_segundo_apellido);
        txtDireccion = (EditText) findViewById(R.id.txt_direccion);
        txtTelefono = (EditText) findViewById(R.id.txt_telefono);


        tipoDocumento = new TipoDocumento(this);
        ArrayAdapter spinnerAdapterTipoDocumento = new ArrayAdapter(this, android.R.layout.simple_spinner_item,android.R.id.text1, tipoDocumento.getAll());
        spinnerAdapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoDocumento.setAdapter(spinnerAdapterTipoDocumento);

        // se carga el spinner pais
        pais = new Pais(this);
        listadoPais = pais.getAll();
        ArrayAdapter adapter = new ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_spinner_item, android.R.id.text1, listadoPais);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPais.setAdapter(adapter);

        // Evento Spinner Pais
        spnPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1){
                    final Pais pais = (Pais)parent.getItemAtPosition(position);

                    TaskCargarSpinner taskCargarSpinner = new TaskCargarSpinner(spnDepartamento,pais.getIdPais(),departamento);
                    taskCargarSpinner.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spnDepartamento.setAdapter(null);
                spnMunicipio.setAdapter(null);
            }
        });

        // Evento Spinner Departamento
        spnDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    final Departamento departamento = (Departamento) parent.getItemAtPosition(position);
                    TaskCargarSpinner taskCargarSpinner = new TaskCargarSpinner(spnMunicipio, departamento.getIdDepartamento(),municipio);
                    taskCargarSpinner.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (spnMunicipio.getAdapter() != null)
                    spnMunicipio.setAdapter(null);
            }
        });

        APIRest.Async.get(APIRest.URL_PERFIL, new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                Perfil[] listPerfil = APIRest.serializeObjectFromJson(json,Perfil[].class);
                if (listPerfil != null && listPerfil.length > 0){
                    visualizarPerfil(listPerfil[0]);
                }
            }

            @Override
            public void onError(String message_error, APIValidations apiValidations) {
                if (apiValidations != null ) {
                    // Se cierra la sessión si el token es invalido
                    if (apiValidations.isTokenInvalid()) {
                        sessionManager.closeSessionExpired(EditProfileActivity.this);
                    }
                    Toast.makeText(EditProfileActivity.this,apiValidations.getResponse(),Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(EditProfileActivity.this,message_error,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void visualizarPerfil(Perfil perfil){
        txtNit.setText(String.valueOf(perfil.getNit()));
        txtPrimerNombre.setText(perfil.getPrimerNombre());
        txtSegundoNombre.setText(perfil.getSegundoNombre());
        txtPrimerApellido.setText(perfil.getPrimerApellido());
        txtSegundoApellido.setText(perfil.getSegundoApellido());
        txtDireccion.setText(perfil.getDireccion());
        txtTelefono.setText(perfil.getTelefono());



        if (perfil.getIdPais() > 0){
            for (int i=0; i<listadoPais.size();i++) {
                if (listadoPais.get(i).getIdPais() == perfil.getIdPais()) {
                    spnPais.setSelection(i+1);
                    break;
                }
            }
        }

        if (perfil.getIdDepartamento() > 0){
            departamento = new Departamento(this);
            departamento.getById(perfil.getIdDepartamento());
        }
        if (perfil.getIdMunicipio() > 0){
            municipio = new Municipio(this);
            municipio.getById(perfil.getIdMunicipio());

        }
    }


    private List<Departamento> cargarDepartamentos(int idPais){
        Departamento departamento = new Departamento(EditProfileActivity.this);
        listadoDepartamento = departamento.getByIdPais(idPais);
        if (listadoDepartamento == null){
            Synchronize synchronize= new Synchronize(EditProfileActivity.this);
            if(synchronize.SynchronizeDepartamento(idPais) > 0){
                listadoDepartamento = departamento.getByIdPais(idPais);
            }
        }
        return listadoDepartamento;
    }

    private List<Municipio> cargarMunicipio(int idDeparamento){
        Municipio municipio = new Municipio(EditProfileActivity.this);
        listadoMunicipio = municipio.getByIdDepartamento(idDeparamento);
        if (listadoMunicipio == null){
            Synchronize synchronize= new Synchronize(EditProfileActivity.this);
            if(synchronize.SynchronizeMunicipio(idDeparamento) > 0){
                listadoMunicipio = municipio.getByIdDepartamento(idDeparamento);
            }
        }
        return listadoMunicipio;
    }

    private class TaskCargarSpinner<T> extends AsyncTask<T,Integer,Boolean>{
        private List<T> listado;
        private T objectSelection;
        private Spinner spinner;
        private int idSpinner;
        private int idFiltro;


        public TaskCargarSpinner(Spinner spinner,int idFiltro, T objectSelection){
            this.idSpinner = spinner.getId();
            this.idFiltro = idFiltro;
            this.spinner = spinner;
            this.objectSelection = objectSelection;
        }

        @Override
        protected Boolean doInBackground(T... params){
            if (idSpinner == R.id.spn_departamento)
                listado = (List<T>)cargarDepartamentos(idFiltro);
            else if(idSpinner == R.id.spn_municipio)
                listado = (List<T>)cargarMunicipio(idFiltro);

            if (listado != null)
                return true;
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success){
            if (success) {
                ArrayAdapter adapter = new ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_spinner_item, android.R.id.text1, listado);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                if (objectSelection != null) {
                    int position= adapter.getPosition(objectSelection);
                    spinner.setSelection(position);
                }
            }
        }
    }
}
