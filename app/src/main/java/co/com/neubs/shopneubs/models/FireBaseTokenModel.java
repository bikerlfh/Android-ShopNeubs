package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 7/19/17.
 */

public class FireBaseTokenModel {
    public static final String NAME_TABLE = "FireBaseTokenModel";
    public static final String PK = "idFireBaseToken";
    public static final String TOKEN = "token";

    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key AUTOINCREMENT not null,"+
            TOKEN + " text not null)";
}
