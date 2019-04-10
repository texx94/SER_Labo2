package ch.heigvd.ser.labo2;

class Main {

    private static final String xmlFilePath = "tournois_fse.xml";

    public static void main(String ... args) {
        // TODO : A implémenter...
        Parser parser = new Parser();
        parser.parse(xmlFilePath);
    }
}
