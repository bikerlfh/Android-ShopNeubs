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
import android.widget.Button;
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
    private Button btnGuardar;


    private List<TipoDocumento> listadoTipoDocumento;
    private List<Pais> listadoPais;
    private Perfil perfil;



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
        btnGuardar = (Button) findViewById(R.id.btn_guardar_perfil);

        listadoTipoDocumento = new TipoDocumento(this).getAll();
        ArrayAdapter spinnerAdapterTipoDocumento = new ArrayAdapter(this, android.R.layout.simple_spinner_item,android.R.id.text1, listadoTipoDocumento);
        spinnerAdapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoDocumento.setAdapter(spinnerAdapterTipoDocumento);

        // se carga el spinner pais
        listadoPais = new Pais(this).getAll();
        ArrayAdapter adapter = new ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_spinner_item, android.R.id.text1, listadoPais);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPais.setAdapter(adapter);

        // Se asignand los eventos a los spinner y el boton
        setEventosControles();

        // Se realiza la petición del perfil
        APIRest.Async.get(APIRest.URL_PERFIL, new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                Perfil[] listPerfil = APIRest.serializeObjectFromJson(json,Perfil[].class);
                if (listPerfil != null && listPerfil.length > 0){
                    perfil = listPerfil[0];
                    visualizarPerfil(perfil);
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

    /**
     * Asigna el evento setOnItemSelectedListener a los spinner y el onclick al boton
     */
    private void setEventosControles(){

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });

        // Evento Spinner Pais
        spnPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1){
                    final Pais pais = (Pais)parent.getItemAtPosition(position);

                    TaskCargarSpinner taskCargarSpinner = new TaskCargarSpinner(spnDepartamento,pais.getIdPais(),perfil != null?perfil.getIdDepartamento():0);
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
                    TaskCargarSpinner taskCargarSpinner = new TaskCargarSpinner(spnMunicipio, departamento.getIdDepartamento(),perfil != null?perfil.getIdMunicipio():0);
                    taskCargarSpinner.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (spnMunicipio.getAdapter() != null)
                    spnMunicipio.setAdapter(null);
            }
        });
    }

    private void guardar(){
        boolean cancel = false;

        spnTipoDocumento.setError(null);
        txtNit.setError(null);
        txtPrimerNombre.setError(null);
        txtSegundoNombre.setError(null);
        txtPrimerApellido.setError(null);
        txtSegundoApellido.setError(null);
        txtDireccion.setError(null);
        txtTelefono.setError(null);
        spnPais.setError(null);
        spnDepartamento.setError(null);
        spnMunicipio.setError(null);
        View focusView = null;

        if (spnTipoDocumento.getSelectedItemPosition() == 0){
            spnTipoDocumento.setError(getString(R.string.error_field_required));
            focusView = spnTipoDocumento;
        }
        if (txtNit.getText().toString().isEmpty()){
            txtNit.setError(getString(R.string.error_field_required));
            focusView = focusView != null? focusView : txtNit;
        }
        if (txtPrimerNombre.getText().toString().isEmpty()){
            txtPrimerNombre.setError(getString(R.string.error_field_required));
            focusView = focusView != null? focusView :txtPrimerNombre;
        }
        if (txtSegundoNombre.getText().toString().isEmpty()){
            txtSegundoNombre.setError(getString(R.string.error_field_required));
            focusView = focusView != null? focusView :txtSegundoNombre;
        }
        if (txtPrimerApellido.getText().toString().isEmpty()){
            txtPrimerApellido.setError(getString(R.string.error_field_required));
            focusView = focusView != null? focusView :txtPrimerApellido;
        }
        if (txtSegundoApellido.getText().toString().isEmpty()){
            txtSegundoApellido.setError(getString(R.string.error_field_required));
            focusView = focusView != null? focusView :txtSegundoApellido;
        }
        if (txtDireccion.getText().toString().isEmpty()){
            txtDireccion.setError(getString(R.string.error_field_required));
            focusView = focusView != null? focusView :txtDireccion;
        }
        if (txtTelefono.getText().toString().isEmpty()){
            txtTelefono.setError(getString(R.string.error_field_required));
            focusView = focusView != null? focusView :txtTelefono;
        }
        if (spnPais.getSelectedItemPosition() == 0){
            spnPais.setError(getString(R.string.error_field_required));
            focusView = focusView != null? focusView :spnPais;
        }
        if (spnDepartamento.getSelectedItemPosition() == 0){
            spnDepartamento.setError(getString(R.string.error_field_required));
            focusView = focusView != null? focusView :spnDepartamento;
        }
        if (spnMunicipio.getSelectedItemPosition() == 0){
            spnMunicipio.setError(getString(R.string.error_field_required));
            focusView = focusView != null? focusView :spnMunicipio;
        }
        if (focusView != null){
            focusView.requestFocus();
        }
        else{

        }
    }

    private void visualizarPerfil(Perfil perfil){
        txtNit.setText(String.valueOf(perfil.getNit()));
        txtPrimerNombre.setText(perfil.getPrimerNombre());
        txtSegundoNombre.setText(perfil.getSegundoNombre());
        txtPrimerApellido.setText(perfil.getPrimerApellido());
        txtSegundoApellido.setText(perfil.getSegundoApellido());
        txtDireccion.setText(perfil.getDireccion());
        txtTelefono.setText(perfil.getTelefono());
        //Se selecciona el pais
        if (perfil.getIdPais() > 0)
            spnPais.setSelection(getItemPosition(listadoPais,perfil.getIdPais()));
        if (perfil.getIdTipoDocumento()>0)
            spnTipoDocumento.setSelection(getItemPosition(listadoTipoDocumento,perfil.getIdTipoDocumento()));
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
            if(synchronize.SynchronizeMunicipio(idDeparamento) > 0){
                listadoMunicipio = municipio.getByIdDepartamento(idDeparamento);
            }
        }
        return listadoMunicipio;
    }

    /**
     * Retorna la posicion del item en el listado
     * @param listado
     * @param id del objeto a buscar
     * @param <T> tipo
     * @return
     */
    private <T> int getItemPosition(List<T> listado,int id){
        if (listado.size() > 0){
            for (int i=0; i<listado.size();i++) {
                if (listado.get(0).getClass() == TipoDocumento.class){
                    if (((TipoDocumento)listado.get(i)).getIdTipoDocumento() ==id)
                        return i+1;
                }
                else if (listado.get(0).getClass() == Pais.class){
                    if (((Pais)listado.get(i)).getIdPais() ==id)
                        return i+1;
                }
                else if(listado.get(0).getClass() == Departamento.class ){
                    if (((Departamento)listado.get(i)).getIdDepartamento() == id)
                        return i+1;
                }
                else if(listado.get(0).getClass() == Municipio.class){
                    if (((Municipio)listado.get(i)).getIdMunicipio() ==id)
                        return i+1;
                }
            }
        }
        return -1;
    }

    private class TaskCargarSpinner<T> extends AsyncTask<T,Integer,Boolean>{
        private Spinner spinner;
        private List<T> listado;
        private int idItemSelected;
        private int idSpinner;
        private int idFiltro;

        /**
         * Async task para cargar los spinner
         * @param spinner el spinner
         * @param idFiltro la carga de algunos spinner dependen de algun filtro. Este es el id del filtro
         * @param idItemSelected el id del elemento que se desea que sea seleccionado cuando se cargue el spinner
         */
        public TaskCargarSpinner(Spinner spinner,int idFiltro, int idItemSelected){
            this.idSpinner = spinner.getId();
            this.idFiltro = idFiltro;
            this.spinner = spinner;
            this.idItemSelected = idItemSelected;
        }

        @Override
        protected Boolean doInBackground(T... params){
            if (idSpinner == R.id.spn_departamento)
                listado = (List<T>)cargarDepartamentos(idFiltro);
            else if(idSpinner == R.id.spn_municipio)
                listado = (List<T>)cargarMunicipio(idFiltro);

            return (listado != null);
        }

        @Override
        protected void onPostExecute(Boolean success){
            if (success) {
                ArrayAdapter adapter = new ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_spinner_item, android.R.id.text1, listado);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                if (idItemSelected > 0) {
                    spinner.setSelection(getItemPosition(listado,idItemSelected));
                }
            }
        }
    }
}
