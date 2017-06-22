package co.com.neubs.shopneubs;

import java.util.ArrayList;

/**
 * Created by Tatiana on 21/06/2017.
 */

public class Section {
    public static char[] getSubTitle;
    private int id;
    private String title;
    private String subTitle;
    private ArrayList<Item>items;

    public Section(){
        id=0;
        title="";
        subTitle="";
        items=new ArrayList<>();
    }
    public Section(int id,String title,String subTitle,ArrayList items){
        this.id=id;
        this.title=title;
        this.subTitle=subTitle;
        this.items=items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public  static  ArrayList<Section>getSection(){
        ArrayList<Section>listSection= new ArrayList<>();
        for (int i=1; i<=2; i++){
            Section section = new Section();
            section.setId(i);
            section.setTitle("OFERTAS");
            section.setSubTitle("Las mejores ofertas aquí");
            ArrayList<Item>items=new ArrayList<>();

            for (int j=1; j<=5;j++){
                items.add(new Item(j,"Producto"));
            }
            section.setItems(items);
            listSection.add(section);
        }
        return listSection;
    }

}
