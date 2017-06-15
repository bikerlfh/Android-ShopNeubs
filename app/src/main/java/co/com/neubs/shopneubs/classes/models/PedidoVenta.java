package co.com.neubs.shopneubs.classes.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bikerlfh on 6/12/17.
 */

public class PedidoVenta implements Serializable {

    @SerializedName("pk")
    private int idPedidoVenta;
    private int numeroPedido;
    @SerializedName("estadoPedidoVenta")
    private String estado;
    private String fecha;
    private int numeroProductos;
    private float valorTotal;
    private String urlDetalle;

    @SerializedName("pedidoVentaPosicion")
    private ArrayList<PedidoVentaPosicion> pedidoVentaPosicion;

    public int getIdPedidoVenta() {
        return idPedidoVenta;
    }

    public void setIdPedidoVenta(int idPedidoVenta) {
        this.idPedidoVenta = idPedidoVenta;
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getNumeroProductos() {
        return numeroProductos;
    }

    public void setNumeroProductos(int numeroProductos) {
        this.numeroProductos = numeroProductos;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getUrlDetalle() {
        return urlDetalle;
    }

    public void setUrlDetalle(String urlDetalle) {
        this.urlDetalle = urlDetalle;
    }

    public ArrayList<PedidoVentaPosicion> getPedidoVentaPosicion() {
        return pedidoVentaPosicion;
    }

    public void setPedidoVentaPosicion(ArrayList<PedidoVentaPosicion> pedidoVentaPosicion) {
        this.pedidoVentaPosicion = pedidoVentaPosicion;
    }
}
