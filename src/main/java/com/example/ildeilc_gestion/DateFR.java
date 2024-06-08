package com.example.ildeilc_gestion;

public class DateFR implements Comparable<DateFR> {

    private int jour;
    private int mois;
    private int annee;

    public DateFR(int jour, int mois, int annee) {
        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }

    @Override
    public String toString() {
        return String.format("%02d - %02d - %d", jour, mois, annee);
    }

    @Override
    public int compareTo(DateFR date) {
        if (annee != date.annee) {
            return Integer.compare(annee, date.annee);
        } else if (mois != date.mois) {
            return Integer.compare(mois, date.mois);
        } else {
            return Integer.compare(jour, date.jour);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        DateFR date = (DateFR) obj;
        return jour == date.jour && mois == date.mois && annee == date.annee;
    }

    @Override
    public int hashCode() {
        int result = jour;
        result = 31 * result + mois;
        result = 31 * result + annee;
        return result;
    }
}
