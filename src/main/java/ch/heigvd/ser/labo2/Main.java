/**
 * SER - LABO 02
 * Authors: Edoardo Carpita, Loris Gilliand, Nicodème Stalder
 * Date: 14-04-2019
 * File: Main.java
 *
 */

package ch.heigvd.ser.labo2;

class Main {

    private static final String xmlFilePath = "tournois_fse.xml";

    public static void main(String ... args) {

        Parser parser = new Parser();
        parser.parse(xmlFilePath);
    }
}
