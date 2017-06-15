package co.com.neubs.shopneubs.classes.models;

/**
 * Created by bikerlfh on 6/15/17.
 */

public class PedidoVentaPosicion {
    private long idPedidoVentaPosicion;
    private Producto producto;
    private int cantidad;
    private int idSaldoInventario;
    private float costoTotal;
    private boolean cancelado;
    private String motivoCancelacionPedidoVenta;

    public long getIdPedidoVentaPosicion() {
        return idPedidoVentaPosicion;
    }

    public void setIdPedidoVentaPosicion(long idPedidoVentaPosicion) {
        this.idPedidoVentaPosicion = idPedidoVentaPosicion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdSaldoInventario() {
        return idSaldoInventario;
    }

    public void setIdSaldoInventario(int idSaldoInventario) {
        this.idSaldoInventario = idSaldoInventario;
    }

    public float getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(float costoTotal) {
        this.costoTotal = costoTotal;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public String getMotivoCancelacionPedidoVenta() {
        return motivoCancelacionPedidoVenta;
    }

    public void setMotivoCancelacionPedidoVenta(String motivoCancelacionPedidoVenta) {
        this.motivoCancelacionPedidoVenta = motivoCancelacionPedidoVenta;
    }
}
