package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=======Test1: Seller findById======= ");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("\n=======Test2: Seller findByDepartment======= ");
        Department dep = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(dep);
        for (Seller s : list){
            System.out.println(s);
        }
        System.out.println("\n=======Test3: Seller findAll======= ");
        List<Seller> listAll = sellerDao.findAll();
        for (Seller s : listAll) {
            System.out.println(s);
        }

        

        
        System.out.println(seller);
    }
}
