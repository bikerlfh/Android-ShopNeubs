package co.com.neubs.shopneubs.classes.models;

import java.util.ArrayList;

/**
 * Created by Tatiana on 21/06/2017.
 */

public class Section {
    private String title;
    private String subTitle;

    // url de los productos
    private String urlProductos;

    private String urlMas;

    public Section() {
    }

    public Section(String title, String subTitle, String urlProductos,String urlMas){
        this.title = title;
        this.subTitle = subTitle;
        this.urlProductos = urlProductos;
        this.urlMas = urlMas;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getUrlProductos() {
        return urlProductos;
    }

    public void setUrlProductos(String urlProductos) {
        this.urlProductos = urlProductos;
    }

    public String getUrlMas() {
        return urlMas;
    }

    public void setUrlMas(String urlMas) {
        this.urlMas = urlMas;
    }

    /**
     * Retorna las secciones staticas
     * @return
     */
    public static ArrayList<Section> getAllSections(){
        ArrayList<Section>listSection = new ArrayList<>();
        listSection.add(new Section("Oferta","Las mejores ofertas aquí","index-oferta/",""));
        listSection.add(new Section("Más Destacados","Los productos más vistos","index-mas-vistos/",""));
        return listSection;
    }

}
