package model;

/**
 *
 * @author EmanuelHurt
 */
import java.sql.Date;

public class Product {
    
    private int idProduct;
    private String nameProduct;
    private String pharmaForm;
    private String concentration;
    private Date dueDate;
    private double price;
    private int existence;
    private String codProduct;
    private String numLote;
    private int idProvider;
    private String nameProvider;
    private String genericName;
    
    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public String getPharmaForm() {
        return pharmaForm;
    }

    public String getConcentration() {
        return concentration;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Double getPrice() {
        return price;
    }

    public int getExistence() {
        return existence;
    }

    public String getCodProduct() {
        return codProduct;
    }

    public String getNumLote() {
        return numLote;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public void setPharmaForm(String pharmaForm) {
        this.pharmaForm = pharmaForm;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setExistence(int existence) {
        this.existence = existence;
    }

    public void setCodProduct(String codProduct) {
        this.codProduct = codProduct;
    }

    public void setNumLote(String numLote) {
        this.numLote = numLote;
    }

    public int getIdProvider() {
        return idProvider;
    }

    public void setIdProvider(int idProvider) {
        this.idProvider = idProvider;
    }

    public String getNameProvider() {
        return nameProvider;
    }

    public void setNameProvider(String nameProvider) {
        this.nameProvider = nameProvider;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }
    
}
