package gacglc.app;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by guigo on 30/10/2017.
 */

@DatabaseTable(tableName = "Gares")
public class Gare implements Serializable {
    @DatabaseField(id = true)
    String intitule_gare;
    String commune;


    public Gare () {}

    public Gare(String intitule_gare, String commune) {
        this.intitule_gare = intitule_gare;
        this.commune = commune;
    }
}
