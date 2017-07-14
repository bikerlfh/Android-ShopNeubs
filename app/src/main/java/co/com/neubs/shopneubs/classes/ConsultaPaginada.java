package co.com.neubs.shopneubs.classes;

import java.util.ArrayList;

import co.com.neubs.shopneubs.classes.models.Marca;
import co.com.neubs.shopneubs.classes.models.SaldoInventario;

/**
 * Created by bikerlfh on 5/24/17.
 * Representa la estructura de la consulta paginada a la API
 */

public class ConsultaPaginada {
    private int count;
    private String next;
    private String previous;
    private ArrayList<SaldoInventario> results;
    // marcas de los productos que vienen en el results
    private ArrayList<Marca> marcas;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        if (next != null) {
            next = APIRest.constructURL(next);
        }
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public ArrayList<SaldoInventario> getResults() {
        return results;
    }

    public void setResults(ArrayList<SaldoInventario> results) {
        this.results = results;
    }

    public ArrayList<Marca> getMarcas() {
        return marcas;
    }

    public void setMarcas(ArrayList<Marca> marcas) {
        this.marcas = marcas;
    }
}
