package com.life_compteur;

import android.os.Parcel;
import android.os.Parcelable;

public class Coup implements Parcelable {

    private Integer lifePlayer1;

    private String signeModificationPlayer1;

    private Integer lifeModificationPlayer1;

    private Integer lifePlayer2;

    private String signeModificationPlayer2;

    private Integer lifeModificationPlayer2;

    public Coup(Integer lifePlayer1, String signeModificationPlayer1, Integer lifeModificationPlayer1, Integer lifePlayer2, String signeModificationPlayer2, Integer lifeModificationPlayer2) {
        this.lifePlayer1 = lifePlayer1;
        this.signeModificationPlayer1 = signeModificationPlayer1;
        this.lifeModificationPlayer1 = lifeModificationPlayer1;
        this.lifePlayer2 = lifePlayer2;
        this.signeModificationPlayer2 = signeModificationPlayer2;
        this.lifeModificationPlayer2 = lifeModificationPlayer2;
    }

    protected Coup(Parcel in) {
        if (in.readByte() == 0) {
            lifePlayer1 = null;
        } else {
            lifePlayer1 = in.readInt();
        }
        signeModificationPlayer1 = in.readString();
        if (in.readByte() == 0) {
            lifeModificationPlayer1 = null;
        } else {
            lifeModificationPlayer1 = in.readInt();
        }
        if (in.readByte() == 0) {
            lifePlayer2 = null;
        } else {
            lifePlayer2 = in.readInt();
        }
        signeModificationPlayer2 = in.readString();
        if (in.readByte() == 0) {
            lifeModificationPlayer2 = null;
        } else {
            lifeModificationPlayer2 = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (lifePlayer1 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(lifePlayer1);
        }
        dest.writeString(signeModificationPlayer1);
        if (lifeModificationPlayer1 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(lifeModificationPlayer1);
        }
        if (lifePlayer2 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(lifePlayer2);
        }
        dest.writeString(signeModificationPlayer2);
        if (lifeModificationPlayer2 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(lifeModificationPlayer2);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Coup> CREATOR = new Creator<Coup>() {
        @Override
        public Coup createFromParcel(Parcel in) {
            return new Coup(in);
        }

        @Override
        public Coup[] newArray(int size) {
            return new Coup[size];
        }
    };

    public Integer getLifePlayer1() {
        return lifePlayer1;
    }

    public String getSigneModificationPlayer1() {
        return signeModificationPlayer1;
    }

    public Integer getLifeModificationPlayer1() {
        return lifeModificationPlayer1;
    }

    public Integer getLifePlayer2() {
        return lifePlayer2;
    }

    public String getSigneModificationPlayer2() {
        return signeModificationPlayer2;
    }

    public Integer getLifeModificationPlayer2() {
        return lifeModificationPlayer2;
    }

    public String toString() {
        int symboleP1 = ("+".equals(this.signeModificationPlayer1) ? 1 : -1);
        int symboleP2 = ("+".equals(this.signeModificationPlayer2) ? 1 : -1);
        if (symboleP1 == 1 || symboleP2 == 1) {
            if (symboleP1 == 1) {
                return (lifePlayer1 + " +" + (lifeModificationPlayer1 * symboleP1) + "         " + lifePlayer2 + " " + lifeModificationPlayer2 * symboleP2);
            } else {
                return (lifePlayer1 + " " + (lifeModificationPlayer1 * symboleP1) + "         " + lifePlayer2 + " +" + lifeModificationPlayer2 * symboleP2);
            }
        }
        return (lifePlayer1 + " " + (lifeModificationPlayer1 * symboleP1) + "         " + lifePlayer2 + " " + lifeModificationPlayer2 * symboleP2);
    }

}
