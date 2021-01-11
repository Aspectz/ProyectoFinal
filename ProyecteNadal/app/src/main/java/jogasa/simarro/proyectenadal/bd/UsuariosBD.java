package jogasa.simarro.proyectenadal.bd;

import java.io.Serializable;
import java.util.ArrayList;

import jogasa.simarro.proyectenadal.pojo.Usuario;

public class UsuariosBD implements Serializable {

    private static ArrayList<Usuario> usuarios=new ArrayList<Usuario>();





    public static ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public static void setUsuarios(ArrayList<Usuario> usuarios) {
        UsuariosBD.usuarios = usuarios;
    }
}
