package model;

/**
 *
 * @author EmanuelHurt
 */
import java.sql.Date;
import java.sql.Time;

public class SaleDetail {
    
    private int idSaleDetail;
    private Date dateSale;
    private double totalPrice;
    private String description;
    private String codClient;
    private Time hourSale;
    
    private String nameClient;

    public int getIdSaleDetail() {
        return idSaleDetail;
    }

    public void setIdSaleDetail(int idSaleDetail) {
        this.idSaleDetail = idSaleDetail;
    }

    public Date getDateSale() {
        return dateSale;
    }

    public void setDateSale(Date dateSale) {
        this.dateSale = dateSale;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodClient() {
        return codClient;
    }

    public void setCodClient(String codClient) {
        this.codClient = codClient;
    }

    public Time getHourSale() {
        return hourSale;
    }

    public void setHourSale(Time hourSale) {
        this.hourSale = hourSale;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }
    
}
