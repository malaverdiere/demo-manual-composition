/*
    This file is part of demo-manual-composition.

    Demo-manual-composition is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Demo-manual-composition is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with demo-manual-composition.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2015 Ecole Polytechnique de Montreal & Tata Consultancy Services
 */
package ca.polymtl.gigl.casi.jaxws.manual.composition;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.soap.SOAPBinding;
import java.math.BigDecimal;
import java.util.*;

@WebService(serviceName = "OrderService")
public class OrderService {


    static final String endpointUrlBase = "http://localhost:8080/jaxws-manual-composition-basic-services/";

    @WebServiceRef(wsdlLocation = endpointUrlBase + "DiscountService?wsdl")
    private DiscountService discountService;

    @WebServiceRef(wsdlLocation = endpointUrlBase + "ShippingServiceImplService?wsdl")
    private ShippingServiceImplService shippingService;

    @WebServiceRef(wsdlLocation = endpointUrlBase + "EventReportingImplService?wsdl")
    private EventReportingImplService eventService;

    @WebMethod
    public Quote prepareQuote(
            @WebParam(name = "items") Item[] items,
            @WebParam(name = "coupons") String[] couponCodes,
            @WebParam(name = "shipTo") Address shippingAddress) {
        if (shippingAddress == null) throw new RuntimeException("No shipping address");
        if (items == null) throw new RuntimeException("No items - null");
        if (items.length == 0) throw new RuntimeException("No items");
        if (couponCodes == null) throw new RuntimeException("No coupons - null");

        try {
            DiscountCalculator ds = discountService.getPort(DiscountCalculator.class);
            ShippingService ss = shippingService.getPort(ShippingService.class);
            EventReportingService er = eventService.getPort(EventReportingService.class);

            //Business logic starts here
            float weight = 0;
            float size = 0;
            BigDecimal subTotal = BigDecimal.ZERO;
            for (Item item : items) {
                subTotal = subTotal.add(item.price.multiply(BigDecimal.valueOf(item.numOrdered)));
                weight += item.weight;
                size += item.size;
            }

            Collection<Discount> discounts = new ArrayList<Discount>();
            for (String code : couponCodes) {
                discounts.add(ds.lookupCoupon(code));
            }

            //Discount logic. Apply the percentage discounts after removing the
            //Fixed currency discounts.
            BigDecimal discountedSubtotal = BigDecimal.ZERO;
            for (Discount discount : discounts) {
                discountedSubtotal = discountedSubtotal.add(discount.getCurrencyOff());
            }

            BigDecimal tmpSubTotal = subTotal.subtract(discountedSubtotal);
            for (Discount discount : discounts) {
                discountedSubtotal = discountedSubtotal.add(tmpSubTotal.multiply(discount.getPercentOff().divide(BigDecimal.valueOf(100))));
            }


            //This is a rough approximation and ignores the federal/provincial split
            BigDecimal taxableAmount = subTotal.subtract(discountedSubtotal);
            BigDecimal taxes = taxableAmount.multiply(new BigDecimal("0.13"));


            //Add 2% to weight and size because of packaging
            BigDecimal shippingCost = ss.estimate(shippingAddress, weight * 1.02f, size * 1.02f);

            BigDecimal total = taxableAmount.add(taxes).add(shippingCost);

            Quote theQuote = new Quote(
                    UUID.randomUUID().toString(), items, discounts.toArray(new Discount[discounts.size()]),
                    discountedSubtotal, taxes, shippingCost, total
            );


            er.reportEvent("Order-Service", "Generated quote #" + theQuote.quoteId,
                           DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()).toXMLFormat());


            return theQuote;
        } catch (DatatypeConfigurationException e){
            throw new RuntimeException(e);
        }

    }

}
