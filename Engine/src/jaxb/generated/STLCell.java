//
// This file was generated by the Eclipse Implementation of JAXB, v4.0.5 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
//


package jaxb.generatedv2;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType>
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element ref="{}STL-Original-Value"/>
 *       </sequence>
 *       <attribute name="row" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       <attribute name="column" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "stlOriginalValue"
})
@XmlRootElement(name = "STL-Cell")
public class STLCell {

    @XmlElement(name = "STL-Original-Value", required = true)
    protected String stlOriginalValue;
    @XmlAttribute(name = "row", required = true)
    protected int row;
    @XmlAttribute(name = "column", required = true)
    protected String column;

    /**
     * Gets the value of the stlOriginalValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTLOriginalValue() {
        return stlOriginalValue;
    }

    /**
     * Sets the value of the stlOriginalValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTLOriginalValue(String value) {
        this.stlOriginalValue = value;
    }

    /**
     * Gets the value of the row property.
     * 
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the value of the row property.
     * 
     */
    public void setRow(int value) {
        this.row = value;
    }

    /**
     * Gets the value of the column property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumn() {
        return column;
    }

    /**
     * Sets the value of the column property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumn(String value) {
        this.column = value;
    }

}
