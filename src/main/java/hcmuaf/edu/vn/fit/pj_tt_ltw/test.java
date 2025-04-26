package hcmuaf.edu.vn.fit.pj_tt_ltw;

import hcmuaf.edu.vn.fit.pj_tt_ltw.DAO.DAOFactory;
import hcmuaf.edu.vn.fit.pj_tt_ltw.DAO.IProductDAO;
import hcmuaf.edu.vn.fit.pj_tt_ltw.Model.Product;

import java.util.List;

public class test {
    public static void main(String[] args) {
        IProductDAO productdao = DAOFactory.getInstance().getProductdao();

        List<Product> listProducts = productdao.all();
        for (int i = 0; i < listProducts.size(); i++) {
            Product a = listProducts.get(i);
            for (int j = 0; j < a.getListimg().size(); j++) {
                System.out.println(a.getListimg().get(j));
            }

        }
    }
}
