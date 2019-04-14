/**
 * SER - LABO 02
 * Authors: Edoardo Carpita, Loris Gilliand, Nicodème Stalder
 * Date: 14-04-2019
 * File: CoupSpecial.java
 *
 */

package ch.heigvd.ser.labo2.coups;

public enum CoupSpecial implements ConvertissableEnPGN {

    ECHEC("+"),
    MAT("#"),
    NULLE("");

    private final String notation;

    CoupSpecial(String notation) {
        this.notation = notation;
    }

    public String notationPGN() {
        return notation;
    }
}
