package gacglc.app;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by guigo on 30/10/2017.
 */

@DatabaseTable(tableName = "MyGares")
public class Gare implements Serializable, Parcelable {
    @DatabaseField(id = true)
    String intitule_gare;
    @DatabaseField
    String commune;
    @DatabaseField
    String latitude_wgs84;
    @DatabaseField
    String longitude_wgs84;

    int pos;


    public Gare () {}

    public Gare(String intitule_gare, String commune, String latitude_wgs84, String longitude_wgs84) {
        this.intitule_gare = intitule_gare;
        this.commune = commune;
        this.latitude_wgs84 = latitude_wgs84;
        this.longitude_wgs84 = longitude_wgs84;
    }

    public Gare(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Gare> CREATOR = new Parcelable.Creator<Gare>() {
        public Gare createFromParcel(Parcel in) {
            return new Gare(in);
        }

        public Gare[] newArray(int size) {

            return new Gare[size];
        }

    };

    public void readFromParcel(Parcel in) {
        commune = in.readString();
        intitule_gare = in.readString();
        longitude_wgs84 = in.readString();
        latitude_wgs84 = in.readString();
        pos = in.readInt();


    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commune);
        dest.writeString(intitule_gare);
        dest.writeString(longitude_wgs84);
        dest.writeString(latitude_wgs84);
        dest.writeInt(pos);
    }
}
