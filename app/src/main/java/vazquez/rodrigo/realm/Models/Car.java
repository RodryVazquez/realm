package vazquez.rodrigo.realm.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rodrigo Vazquez on 01/07/2017.
 */

public class Car implements Parcelable {

    private int CarId;
    private String CarName;
    private Boolean isRun;

    public int getCarId() {
        return CarId;
    }

    public void setCarId(int carId) {
        CarId = carId;
    }

    public String getCarName() {
        return CarName;
    }

    public void setCarName(String carName) {
        CarName = carName;
    }

    public Boolean getRun() {
        return isRun;
    }

    public void setRun(Boolean run) {
        isRun = run;
    }

    /**
     *
     * @return
     */
    public String getGeneralInfo(){
        StringBuilder builder = new StringBuilder();
        builder.append(" Car Id is " + this.getCarId());
        builder.append("and Brand is " + this.getCarName());
        return builder.toString();
    }

    public Car(int carId, String carName, Boolean isRun) {
        CarId = carId;
        CarName = carName;
        this.isRun = isRun;
    }

    /**
     * FIFO
     * @param in
     */
    protected Car(Parcel in) {
        this.CarId = in.readInt();
        this.CarName = in.readString();
        this.isRun = in.readByte() == 1 ? true : false;
    }

    /**
     * Necesario para implementar correctamente la interfaz
     */
    public static final Creator<Car> CREATOR = new Creator<Car>() {

        /**
         * Reconstruimos el objeto con base a un objeto de la clase Parcel
         * @param in
         * @return
         */
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Escribmos en un objeto Parcel los elementos de nuestra clase
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.CarId);
        dest.writeString(this.CarName);
        dest.writeByte((byte) (isRun == true ? 1 : 0));
    }
}
