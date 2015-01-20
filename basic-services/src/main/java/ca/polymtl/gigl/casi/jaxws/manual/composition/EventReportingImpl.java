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
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

@WebService(endpointInterface = "ca.polymtl.gigl.casi.jaxws.manual.composition.EventReporting")
public class EventReportingImpl implements EventReporting{

    final static String DATABASE_RESOURCE_NAME = "jdbc/samplecomposition";

    //possible solution for the timestamp mapping: http://blog.bdoughan.com/2012/02/glassfish-312-is-full-of-moxy.html

    @Override
    public void reportEvent(String originatorModule, String event, String timeStamp){
        if (originatorModule == null) throw new RuntimeException("Mandatory originator module");
        if (event == null) throw new RuntimeException("Mandatory event");
        if (timeStamp == null) throw new RuntimeException("Mandatory time stamp");

        try {

            XMLGregorianCalendar tsValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(timeStamp);

            String query = "INSERT INTO events VALUES(?,?,?)";

            DataSource ds = InitialContext.doLookup(DATABASE_RESOURCE_NAME);
            Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setTime(1, new Time(tsValue.toGregorianCalendar().getTimeInMillis()));
            stmt.setString(2, originatorModule);
            stmt.setString(3, event);
            stmt.execute();


        } catch (NamingException | SQLException | DatatypeConfigurationException e ) {
            throw new RuntimeException(e);
        }
    }
}
