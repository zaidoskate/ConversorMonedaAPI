import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

public class Conversor {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/1e854f6883e8ec627755b87a/latest/USD";
    private static final List<ConversionRecord> conversionRecords = new ArrayList<>();

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL)).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            ExchangeRateResponse exchangeRateResponse = gson.fromJson(response.body(), ExchangeRateResponse.class);

            if ("success".equals(exchangeRateResponse.result())) {
                showMenu(exchangeRateResponse.conversionRates());
            } else {
                System.out.println("Error al obtener los datos de la API.");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Hubo un problema con la respuesta del servidor.");
        }
    }

    private static void showMenu(Map<String, Double> conversionRates) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Selecciona una opción:");
            System.out.println("1. Convertir MXN a USD");
            System.out.println("2. Convertir USD a MXN");
            System.out.println("3. Convertir MXN a EUR");
            System.out.println("4. Convertir EUR a MXN");
            System.out.println("5. Otra moneda");
            System.out.println("6. Mostrar historial de conversiones");
            System.out.println("7. Salir");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    convertCurrency(conversionRates, "MXN", "USD", scanner);
                    break;
                case 2:
                    convertCurrency(conversionRates, "USD", "MXN", scanner);
                    break;
                case 3:
                    convertCurrency(conversionRates, "MXN", "EUR", scanner);
                    break;
                case 4:
                    convertCurrency(conversionRates, "EUR", "MXN", scanner);
                    break;
                case 5:
                    System.out.println("Introduce la moneda de origen:");
                    String fromCurrency = scanner.next().toUpperCase();
                    System.out.println("Introduce la moneda de destino:");
                    String toCurrency = scanner.next().toUpperCase();
                    convertCurrency(conversionRates, fromCurrency, toCurrency, scanner);
                    break;
                case 6:
                    showConversionHistory();
                    break;
                case 7:
                    System.out.println("Saliendo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opción no válida. Por favor, intenta de nuevo.");
            }
        }
    }

    private static void convertCurrency(Map<String, Double> conversionRates, String fromCurrency, String toCurrency, Scanner scanner) {
        if (conversionRates.containsKey(fromCurrency) && conversionRates.containsKey(toCurrency)) {
            System.out.println("Introduce la cantidad a convertir:");
            double amount = scanner.nextDouble();

            double fromRate = conversionRates.get(fromCurrency);
            double toRate = conversionRates.get(toCurrency);
            double convertedAmount = (amount / fromRate) * toRate;

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            ConversionRecord record = new ConversionRecord(fromCurrency, toCurrency, amount, convertedAmount, formattedDateTime);
            conversionRecords.add(record);

            System.out.printf("La cantidad de %.2f %s es equivalente a %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);
        } else {
            System.out.println("Una de las monedas no es válida. Por favor, intenta de nuevo.");
        }
    }

    private static void showConversionHistory() {
        if (conversionRecords.isEmpty()) {
            System.out.println("No hay registros de conversiones.");
        } else {
            System.out.println("Historial de conversiones:");
            for (ConversionRecord record : conversionRecords) {
                System.out.printf("Fecha y hora: %s | %s a %s | Cantidad: %.2f | Convertido: %.2f%n",
                        record.getDateTime(), record.getFromCurrency(), record.getToCurrency(),
                        record.getAmount(), record.getConvertedAmount());
            }
        }
    }
}
