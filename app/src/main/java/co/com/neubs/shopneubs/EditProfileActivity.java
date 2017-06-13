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

public class EditProfileActivity extends AppCompatActivity {

    private final SessionManager sessionManager = SessionManager.getInstance();

    private Spinner spnTipoDocumento,spnPais,spnDepartamento,spnMunicipio;
    private EditText txtNit,txtPrimerNombre,txtSegundoNombre,txtPrimerApellido,txtSegundoApellido,txtDireccion,txtTelefono;

    private TipoDocumento tipoDocumento;
    private Pais pais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spnTipoDocumento = (Spinner) findViewById(R.id.spn_tipo_documento);
        spnPais = (Spinner) findViewById(R.id.spn_pais);
        spnDepartamento = (Spinner) findViewById(R.id.spn_departamento);
        spnMunicipio = (Spinner) findViewById(R.id.spn_municipio);

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


        spnTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoDocumento tipoDocumento = (TipoDocumento)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Pais pais = new Pais(this);
        ArrayAdapter adapter = new ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_spinner_item, android.R.id.text1, pais.getAll());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPais.setAdapter(adapter);

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

        // Evento Spinner Pais
        spnPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final Pais pais = (Pais)parent.getItemAtPosition(position);

                TaskCargarSpinner taskCargarSpinner = new TaskCargarSpinner(R.id.spn_departamento,pais.getIdPais());
                taskCargarSpinner.execute();
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
                final Departamento departamento = (Departamento) parent.getItemAtPosition(position);

                TaskCargarSpinner taskCargarSpinner = new TaskCargarSpinner(R.id.spn_municipio,departamento.getIdDepartamento());
                taskCargarSpinner.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spnMunicipio.setAdapter(null);
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
    }


    private List<Departamento> cargarDepartamentos(int idPais){
        Departamento departamento = new Departamento(EditProfileActivity.this);
        List<Departamento> listadoDepartamento = departamento.getByIdPais(idPais);
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
        List<Municipio> listadoMunicipio = municipio.getByIdDepartamento(idDeparamento);
        if (listadoMunicipio == null){
            Synchronize synchronize= new Synchronize(EditProfileActivity.this);
            if(synchronize.SynchronizeDepartamento(idDeparamento) > 0){
                listadoMunicipio = municipio.getByIdDepartamento(idDeparamento);
            }
        }
        return listadoMunicipio;
    }

    private class TaskCargarSpinner extends AsyncTask<Void,Integer,Boolean>{
        private List<Departamento> listadoDepartamento;
        private List<Municipio> listadoMunicipio;

        private int idSpinner;
        private int idFiltro;
        private final int spinnerDepartamento = R.id.spn_departamento;
        private final int spinnerMunicipio = R.id.spn_municipio;


        public TaskCargarSpinner(int idSpinner,int idFiltro){
            this.idSpinner = idSpinner;
            this.idFiltro = idFiltro;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (idSpinner == spinnerDepartamento){
                listadoDepartamento = cargarDepartamentos(idFiltro);
            }
            else if(idSpinner == spinnerMunicipio){
                listadoMunicipio = cargarMunicipio(idFiltro);
            }
            if (listadoDepartamento != null || listadoMunicipio != null)
                return true;
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                ArrayAdapter adapter = null;
                if (listadoDepartamento != null)
                    adapter = new ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_spinner_item, android.R.id.text1, listadoDepartamento);
                else
                    adapter = new ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_spinner_item, android.R.id.text1, listadoMunicipio);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnDepartamento.setAdapter(adapter);
            }
        }
    }
}
