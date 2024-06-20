public class ConversionRecord {
    private final String fromCurrency;
    private final String toCurrency;
    private final double amount;
    private final double convertedAmount;
    private final String dateTime;

    public ConversionRecord(String fromCurrency, String toCurrency, double amount, double convertedAmount, String dateTime) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
        this.dateTime = dateTime;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getAmount() {
        return amount;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }

    public String getDateTime() {
        return dateTime;
    }
}