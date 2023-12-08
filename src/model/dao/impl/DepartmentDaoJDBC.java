package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{
    private static final String FIND_ALL_DEPARTMENTS = "SELECT * FROM department";
    private static final String INSERT_DEPARTMENT = "INSERT INTO department (Name) VALUES (?)";
    private static final String UPDATE_DEPARTMENT = "UPDATE department SET Name = ? WHERE Id = ?";
    private static final String DELETE_DEPARTMENT = "DELETE FROM department WHERE id = ?";
    private static final String FIND_DEPARTMENT_BY_ID = "SELECT * FROM department WHERE Id = ?";



    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        try { 
            st = conn.prepareStatement(INSERT_DEPARTMENT, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getName());
            var rows = st.executeUpdate();
            if (rows > 0){
                ResultSet rs = st.getGeneratedKeys();
                    if (rs.next()) {
                        var id = rs.getInt(1);
                        obj.setId(id);
                    } 
                DB.closeResultSet(rs);
            }else {
                throw new DbException("Unexpected Error");
            }            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
        }

    @Override
    public void update(Department obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(UPDATE_DEPARTMENT);
            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(DELETE_DEPARTMENT);
            st.setInt(1, id);
            st.executeUpdate();
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
       PreparedStatement st = null;
       ResultSet rs = null;
       Department dep = null;
        try {
        st = conn.prepareStatement(FIND_DEPARTMENT_BY_ID, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, id);
        rs = st.executeQuery();
        if (rs.next()) {
            dep = new Department();
            dep.setId(rs.getInt("Id"));
            dep.setName(rs.getString("Name"));
        }
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    return dep;
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        var departmentList = new ArrayList<Department>();

        try {
            st = conn.prepareStatement(FIND_ALL_DEPARTMENTS);
            rs = st.executeQuery();
            while (rs.next()) {
                var department = new Department();
                department.setId(rs.getInt("Id"));
                department.setName(rs.getString("Name"));
                departmentList.add(department);
            }
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    return departmentList;
    }

}
