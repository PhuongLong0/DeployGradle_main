package hcmuaf.edu.vn.fit.pj_tt_ltw.Controller;

import hcmuaf.edu.vn.fit.pj_tt_ltw.DAO.DAOFactory;
import hcmuaf.edu.vn.fit.pj_tt_ltw.DAO.IProductDAO;
import hcmuaf.edu.vn.fit.pj_tt_ltw.Model.Product;
import hcmuaf.edu.vn.fit.pj_tt_ltw.Model.ShoppingCart;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet(name = "Index", value = "/index")
public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//them database vao application
        IProductDAO productdao = DAOFactory.getInstance().getProductdao();
        ServletContext application = getServletContext();
        ShoppingCart cart= new ShoppingCart();
        application.setAttribute("cart", cart);
        application.setAttribute("productdao", productdao);
        List<Product> listProducts= productdao.all();
        System.out.println("Số sản phẩm lấy được: " + listProducts.size()); // Debug
        request.setAttribute("listProducts", listProducts);


        application.setAttribute("listProducts", listProducts);

        application.setAttribute("message", "Hello from Servlet");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/men.jsp");
        dispatcher.forward(request, response);

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}

