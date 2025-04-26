package hcmuaf.edu.vn.fit.pj_tt_ltw.DAO;

import hcmuaf.edu.vn.fit.pj_tt_ltw.Model.Product;

import java.util.List;

public interface IProductDAO {
    public List<Product> all();

    public int insert(Product product);

    public int update(Product product);

    public int delete(Product product);

    public Product findById(String id);

    public int count(String txtSearch);

    public List<Product> searchResult(String txtSearch);
}
