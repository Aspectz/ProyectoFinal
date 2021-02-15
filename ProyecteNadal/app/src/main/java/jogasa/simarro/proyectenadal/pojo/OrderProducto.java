package jogasa.simarro.proyectenadal.pojo;

import java.io.Serializable;

import jogasa.simarro.proyectenadal.dao.OrderProductsDAO;

public class OrderProducto implements Serializable {
    int idOrder, idProducto;

    public OrderProducto(){}

    public OrderProducto(int idOrder, int idProducto){
        this.idOrder=idOrder;
        this.idProducto=idProducto;
    }


    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
}
