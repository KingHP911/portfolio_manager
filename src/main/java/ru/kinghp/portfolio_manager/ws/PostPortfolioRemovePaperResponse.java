//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.04.09 at 05:11:06 PM MSK 
//


package ru.kinghp.portfolio_manager.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="portfolioDto" type="{http://www.ws.portfolio_manager.kinghp.ru}portfolioDto"/>
 *         &lt;element name="papersDto" type="{http://www.ws.portfolio_manager.kinghp.ru}portfoliosPaperDto" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "portfolioDto",
    "papersDto"
})
@XmlRootElement(name = "postPortfolioRemovePaperResponse")
public class PostPortfolioRemovePaperResponse {

    @XmlElement(required = true)
    protected PortfolioDto portfolioDto;
    protected List<PortfoliosPaperDto> papersDto;

    /**
     * Gets the value of the portfolioDto property.
     * 
     * @return
     *     possible object is
     *     {@link PortfolioDto }
     *     
     */
    public PortfolioDto getPortfolioDto() {
        return portfolioDto;
    }

    /**
     * Sets the value of the portfolioDto property.
     * 
     * @param value
     *     allowed object is
     *     {@link PortfolioDto }
     *     
     */
    public void setPortfolioDto(PortfolioDto value) {
        this.portfolioDto = value;
    }

    /**
     * Gets the value of the papersDto property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the papersDto property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPapersDto().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PortfoliosPaperDto }
     * 
     * 
     */
    public List<PortfoliosPaperDto> getPapersDto() {
        if (papersDto == null) {
            papersDto = new ArrayList<PortfoliosPaperDto>();
        }
        return this.papersDto;
    }

}
