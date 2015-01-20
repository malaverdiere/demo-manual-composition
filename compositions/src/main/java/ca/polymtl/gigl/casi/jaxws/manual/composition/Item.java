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

public class Item {

    public long productId;
    public String description;
    public BigDecimal price;
    public int  numOrdered;
    public float weight;
    public float size;

}
