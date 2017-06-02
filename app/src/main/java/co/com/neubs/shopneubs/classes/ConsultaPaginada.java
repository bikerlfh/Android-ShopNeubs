package co.com.neubs.shopneubs.classes;

import java.util.ArrayList;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        // Ajuste temporal mientras se arregla el api
        if (next != null) {
            if (next.contains("http") && !next.contains(APIRest.PROTOCOL_URL_API))
                next = next.replace("http",APIRest.PROTOCOL_URL_API);
            else if (next.contains("https") && !next.contains(APIRest.PROTOCOL_URL_API))
                next = next.replace("https",APIRest.PROTOCOL_URL_API);
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

}
