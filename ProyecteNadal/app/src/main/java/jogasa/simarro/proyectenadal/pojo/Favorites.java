package jogasa.simarro.proyectenadal.pojo;

import java.io.Serializable;

public class Favorites implements Serializable {
    private String idProduct;
    private String idUser;


    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
