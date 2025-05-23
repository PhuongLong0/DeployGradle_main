package hcmuaf.edu.vn.fit.pj_tt_ltw.DAO;

import hcmuaf.edu.vn.fit.pj_tt_ltw.DB.DBConnect;
import hcmuaf.edu.vn.fit.pj_tt_ltw.Model.Categories;
import hcmuaf.edu.vn.fit.pj_tt_ltw.Model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {
    @Override
    public List<Product> all() {
        Connection connect;
        List<Product> res = new ArrayList<>();
        String sql = "SELECT * FROM products ";
        String sqlImage = "SELECT imageUrl FROM product_images WHERE productId = ?";
        try {connect = DBConnect.getConnection();
             Statement statement = connect.createStatement();
             ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("productId"),
                    rs.getString("productName"),
                    rs.getString("productDetail"),
                    rs.getInt("priceSell"));

                // Lấy danh sách ảnh theo productId
                PreparedStatement pre = connect.prepareStatement(sqlImage);
                pre.setInt(1, rs.getInt("productId"));
                ResultSet rsimg = pre.executeQuery();
                while (rsimg.next()) {
                    product.getListimg().add(rsimg.getString("imageUrl"));
                }

                res.add(product);
                }
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    @Override
    public int insert(Product product) {
        String sql = "INSERT INTO products (productName, priceBuy, priceSell, productDetail, brandName, createAt, categoryId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int res = 0;

        try (Connection connect = DBConnect.getConnection();
             PreparedStatement prs = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            prs.setString(1, product.getProductName());
            prs.setDouble(2, product.getPriceBuy());
            prs.setDouble(3, product.getPriceSell());
            prs.setString(4, product.getProductDetail());
            prs.setString(5, product.getBrandName());
            prs.setTimestamp(6, product.getCreateAt());
            prs.setInt(7, product.getCategoryId());

            res = prs.executeUpdate();
            if (res > 0) {
                ResultSet rs = prs.getGeneratedKeys();
                if (rs.next()) {
                    int productId = rs.getInt(1);
                    product.setProductId(productId);
                }
                insertImages(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private int updateCategory(int categoryId) {
        String sql = "update categories set soluong= soluong+1 where categoryId=?";
        Connection connection;
        int res = 0;
        try {
            connection = DBConnect.getConnection();
            PreparedStatement prs = connection.prepareStatement(sql);
            prs.setInt(1, categoryId);
            res = prs.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return res;
    }

    private void insertImage(Product product) {
        String sql = "INSERT INTO products(productId, imageProduct ) VALUES( ?, ?)";
        Connection connect = null;

        try {
            // Kết nối cơ sở dữ liệu
            connect = DBConnect.getConnection();
            PreparedStatement prs = connect.prepareStatement(sql);

            // Duyệt qua danh sách hình ảnh của sản phẩm và chèn từng ảnh vào bảng
            // ProductImages
            for (String img : product.getListimg()) {
                prs.setInt(1, product.getProductId());
                prs.setString(2, img);
                prs.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int update(Product product) {
        String sql = "UPDATE products SET productName = ?, priceBuy = ?, priceSell = ?, productDetail = ?, brandName = ?, categoryId = ? WHERE productId = ?";
        int res = 0;

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement(sql)) {

            prs.setString(1, product.getProductName());
            prs.setInt(2, product.getPriceBuy());
            prs.setInt(3, product.getPriceSell());
            prs.setString(4, product.getProductDetail());
            prs.setString(5, product.getBrandName());
            prs.setInt(6, product.getCategoryId());
            prs.setInt(7, product.getProductId());

            res = prs.executeUpdate();

            // Xóa và cập nhật danh sách ảnh
            deleteImages(product.getProductId());
            insertImages(product);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int delete(Product product) {
        String sql = "DELETE FROM products WHERE productId=? ";
        int res = 0;
        try (Connection connection = DBConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Xóa ảnh trước khi xóa sản phẩm
            deleteImages(product.getProductId());

            preparedStatement.setInt(1, product.getProductId());
            res = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public Product findById(String id) {
        String sql = "SELECT * FROM products WHERE productId = ?";
        Product product = null;

        /*try (Connection connect = DBConnect.getConnection();
             PreparedStatement prs = connect.prepareStatement(sql)) {

            prs.setInt(1, Integer.parseInt(id));
            ResultSet rs = prs.executeQuery();

            if (rs.next()) {
                product = new Products(
                        rs.getInt("productId"),
                        rs.getString("productName"),
                        rs.getInt("priceBuy"),
                        rs.getInt("priceSell"),
                        rs.getString("productDetail"),
                        rs.getString("brandName"),
                        rs.getTimestamp("createAt"),
                        rs.getInt("categoryId")
                );

                // Lấy danh sách ảnh từ bảng product_images
                product.setListimg(getImagesForProduct(product.getProductId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        return product;
    }

    private void insertImages(Product product) {
        String sql = "INSERT INTO product_images (productId, imageProduct) VALUES (?, ?)";
        try (Connection connect = DBConnect.getConnection();
             PreparedStatement prs = connect.prepareStatement(sql)) {

            for (String img : product.getListimg()) {
                prs.setInt(1, product.getProductId());
                prs.setString(2, img);
                prs.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void deleteImages(int productId) {
        String sql = "DELETE FROM product_images WHERE productId=?";
        try (Connection connect = DBConnect.getConnection();
             PreparedStatement prs = connect.prepareStatement(sql)) {

            prs.setInt(1, productId);
            prs.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


private int degreecategory(Product product) {
    String sql = "update categories set soluong= soluong-1 where category=?";
    Connection connection;
    int res = 0;
    try {
        connection = DBConnect.getConnection();
        PreparedStatement prs = connection.prepareStatement(sql);
        prs.setInt(1, product.getCategoryId());
        res = prs.executeUpdate();
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return res;

}


    public List<Categories> getCategories() {
        List<Categories> res = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection connect = DBConnect.getConnection();
             Statement st = connect.createStatement();
             ResultSet resultset = st.executeQuery(sql)) {

            while (resultset.next()) {
                res.add(new Categories(resultset.getString("categoryName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int count(String txtSearch) {
        String query = "SELECT count(*) FROM products WHERE productName LIKE ?";
        int count = 0;
        try (Connection connect = DBConnect.getConnection();
             PreparedStatement preparedStatement = connect.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + txtSearch + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<Product> searchResult(String txtSearch) {
        List<Product> res = new ArrayList<>();
        /*String sql = "SELECT * FROM products WHERE productName LIKE ?";
        try (Connection connect = DBConnect.getConnection();
             PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setString(1, "%" + txtSearch + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Products product = new Products(
                        rs.getInt("productId"),
                        rs.getString("productName"),
                        rs.getInt("priceBuy"),
                        rs.getInt("priceSell"),
                        rs.getString("productDetail"),
                        rs.getString("brandName"),
                        rs.getTimestamp("createAt"),
                        rs.getInt("categoryId")
                );

                product.setListimg(getImagesForProduct(product.getProductId()));
                res.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        return res;

}
    private List<String> getImagesForProduct(int productId) {
        List<String> images = new ArrayList<>();
        String sql = "SELECT imageProduct FROM product_images WHERE productId=?";
        try (Connection connect = DBConnect.getConnection();
             PreparedStatement prs = connect.prepareStatement(sql)) {

            prs.setInt(1, productId);
            ResultSet rs = prs.executeQuery();
            while (rs.next()) {
                images.add(rs.getString("imageProduct"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return images;
    }


    public List<Product> filter(String categoryFilter) {
        List<Product> res = new ArrayList<>();
       /* String sql = "SELECT * FROM products WHERE categoryId = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement prs = conn.prepareStatement(sql)) {

            prs.setString(1, categoryFilter);
            ResultSet rs = prs.executeQuery();

            while (rs.next()) {
                Products product = new Products(
                        rs.getInt("productId"),
                        rs.getString("productName"),
                        rs.getInt("priceBuy"),
                        rs.getInt("priceSell"),
                        rs.getString("productDetail"),
                        rs.getString("brandName"),
                        rs.getTimestamp("createAt"),
                        rs.getInt("categoryId")
                );

                // Lấy danh sách ảnh từ bảng product_images
                product.setListimg(getImagesForProduct(product.getProductId()));

                res.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        return res;
    }

}
