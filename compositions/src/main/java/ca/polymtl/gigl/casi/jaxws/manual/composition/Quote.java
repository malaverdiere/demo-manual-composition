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


import java.math.BigDecimal;

public class Quote {
    public String quoteId;
    public Item[] items;
    public Discount[] discounts;
    public BigDecimal discount;
    public BigDecimal taxes;
    public BigDecimal shipping;
    public BigDecimal total;

    public Quote(String quoteId, Item[] items, Discount[] discounts, BigDecimal discount,
                 BigDecimal taxes, BigDecimal shipping, BigDecimal total) {
        this.quoteId = quoteId;
        this.items = items;
        this.discounts = discounts;
        this.discount = discount;
        this.taxes = taxes;
        this.shipping = shipping;
        this.total = total;
    }
}
