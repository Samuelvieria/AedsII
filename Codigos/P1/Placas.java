import java.util.*;

public class Placas {

    public static void Placas() {
        Scanner scanner = new Scanner(System.in);

        String placa = scanner.nextLine();

        int tam = placa.length();

        if (tam == 8) {

            System.out.println("1");

        } else if (tam == 7) {

            System.out.println("2");

        } else {
            System.out.println("0");
        }

    }

    public static void main(String[] args) {

        Placas();

    }
}