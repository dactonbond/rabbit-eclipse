//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.04 at 03:07:53 PM NZST 
//


package rabbit.data.internal.xml.schema.events;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for taskFileEventType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="taskFileEventType">
 *   &lt;complexContent>
 *     &lt;extension base="{}durationEventType">
 *       &lt;sequence>
 *         &lt;element name="taskId" type="{}taskIdType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="filePath" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taskFileEventType", propOrder = {
    "taskId"
})
public class TaskFileEventType
    extends DurationEventType
{

    @XmlElement(required = true)
    protected TaskIdType taskId;
    @XmlAttribute(required = true)
    protected String filePath;

    /**
     * Gets the value of the taskId property.
     * 
     * @return
     *     possible object is
     *     {@link TaskIdType }
     *     
     */
    public TaskIdType getTaskId() {
        return taskId;
    }

    /**
     * Sets the value of the taskId property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaskIdType }
     *     
     */
    public void setTaskId(TaskIdType value) {
        this.taskId = value;
    }

    /**
     * Gets the value of the filePath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the value of the filePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilePath(String value) {
        this.filePath = value;
    }

}
