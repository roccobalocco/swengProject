
import data.AdminDAO;
import data.AdminDAOImpl;

public class Main {
    public static void main(String[] args){
        AdminDAO ed = AdminDAOImpl.getInstance();

        System.out.println(ed.getAdmin("MSLPTR00S07C623M", "Theangel23").toString());

    }
}

