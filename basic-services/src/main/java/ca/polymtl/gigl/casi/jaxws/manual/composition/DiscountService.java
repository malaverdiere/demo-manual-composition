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


import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebService(targetNamespace = "http://ca.polymtl.gigl.casi.jaxws-manual-composition", name = "DiscountCalculator", serviceName = "DiscountService")
public class DiscountService {

    final static String DATABASE_RESOURCE_NAME = "jdbc/samplecomposition";


    public Discount lookupCoupon(String couponCode) {
        try {

            String query = "SELECT * FROM DISCOUNTS WHERE CODE='" + couponCode.toUpperCase() + "'";
            DataSource ds = InitialContext.doLookup(DATABASE_RESOURCE_NAME);

            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute(query);
            ResultSet rs = stmt.getResultSet();
            rs.next();
            String label = rs.getString("label");
            BigDecimal currency = rs.getBigDecimal("currency_discount");
            BigDecimal percent = rs.getBigDecimal("percent_discount");


            return new Discount(label, currency, percent);
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
