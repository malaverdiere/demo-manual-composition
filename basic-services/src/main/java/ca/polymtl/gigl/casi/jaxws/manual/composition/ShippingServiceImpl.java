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

import de.daenet.webservices.currencyserver.CurrencyServerWebService;
import de.daenet.webservices.currencyserver.CurrencyServerWebServiceSoap;

import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebService(endpointInterface = "ca.polymtl.gigl.casi.jaxws.manual.composition.ShippingService")
public class ShippingServiceImpl {

    final static String DATABASE_RESOURCE_NAME = "jdbc/samplecomposition";

    public BigDecimal calculateEstimate(Address destination, float weight,float size) {

        try {
            if (destination == null) throw new RuntimeException("No destination parameters");
            if (destination.country == null) throw new RuntimeException("Missing country");
            if (weight <= 0) throw new RuntimeException("Positive weight needed");
            if (size <= 0) throw new RuntimeException("Positive size needed");


            String query = "SELECT * FROM shipping where country='" + destination.country.toUpperCase() + "'";

            DataSource ds = InitialContext.doLookup(DATABASE_RESOURCE_NAME);
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute(query);
            ResultSet rs = stmt.getResultSet();
            rs.next();
            BigDecimal rate = rs.getBigDecimal("rate");

            //our suppliers' rates are in USD, convert to CAD
            CurrencyServerWebService srv = new CurrencyServerWebService();
            CurrencyServerWebServiceSoap port = srv.getCurrencyServerWebServiceSoap();
            double factor = port.getCurrencyValue("AVERAGE", "USD", "CAD");


            BigDecimal basePrice = rate.multiply(BigDecimal.valueOf(weight)).multiply(BigDecimal.valueOf(size));
            return basePrice.multiply(BigDecimal.valueOf(factor));

        } catch (NamingException | SQLException e ) {
            throw new RuntimeException(e);
        }
    }
}
