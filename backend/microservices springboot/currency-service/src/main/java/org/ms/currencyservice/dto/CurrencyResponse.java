package org.ms.currencyservice.dto;

import java.util.Map;

public class CurrencyResponse {
	private Map<String, Currency> data;
    public Map<String, Currency> getData() {
        return data;
    }

    public void setData(Map<String, Currency> data) {
        this.data = data;
    }
}
