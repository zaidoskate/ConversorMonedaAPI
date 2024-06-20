import java.util.Map;

import com.google.gson.annotations.SerializedName;

public record ExchangeRateResponse(String result, @SerializedName("conversion_rates") Map<String,Double> conversionRates){}