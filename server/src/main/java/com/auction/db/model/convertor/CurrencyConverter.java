package com.auction.db.model.convertor;


import com.auction.db.model.Currency;
import org.springframework.core.convert.converter.Converter;

public class CurrencyConverter implements Converter<String, Currency> {
    @Override
    public Currency convert(String from){
        if("cad".equals(from))
            return Currency.CAD;
        else if ("usd".equals(from))
            return Currency.USD;
        else
            return null;
    }
}
