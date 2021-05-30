package jogasa.simarro.projectefinal.pojo;

import java.io.Serializable;

public class OrderDetails implements Serializable {
    int idOrder;
    String idProducto;
    float totalPrice;
    int quantity;
    String idOrderDetails;

    public OrderDetails(){}

    public OrderDetails(int idOrder, String idProducto){
        this.idOrder=idOrder;
        this.idProducto=idProducto;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getIdOrderDetails() {
        return idOrderDetails;
    }

    public void setIdOrderDetails(String idOrderDetails) {
        this.idOrderDetails = idOrderDetails;
    }
}
