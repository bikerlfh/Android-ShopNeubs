package co.com.neubs.shopneubs;

/**
 * Created by Tatiana on 21/06/2017.
 */

public class Item {
    private int id;
    private String name;

    public Item(){
        id=0;
        name="";
    }
    public Item(int id,String name){
        this.id=id;
        this.name=name;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
